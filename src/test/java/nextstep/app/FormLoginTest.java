package nextstep.app;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Set;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FormLoginTest {

    private static final Member TEST_ADMIN_MEMBER = new Member("a@a.com", "password", "a", "", Set.of("ADMIN"));
    private static final Member TEST_USER_MEMBER = new Member("b@b.com", "password", "b", "", Set.of("USER"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.save(TEST_ADMIN_MEMBER);
        memberRepository.save(TEST_USER_MEMBER);
    }

    @DisplayName("로그인 성공")
    @Test
    void login_success() throws Exception {
        ResultActions loginResponse = mockMvc.perform(post("/login")
                .param("username", TEST_USER_MEMBER.getEmail())
                .param("password", TEST_USER_MEMBER.getPassword())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        loginResponse
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("로그인 실패 - 사용자 없음")
    @Test
    void login_fail_with_no_user() throws Exception {
        ResultActions response = mockMvc.perform(post("/login")
                .param("username", "none")
                .param("password", "none")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        response.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("로그인 실패 - 비밀번호 불일치")
    @Test
    void login_fail_with_invalid_password() throws Exception {
        ResultActions response = mockMvc.perform(post("/login")
                .param("username", TEST_USER_MEMBER.getEmail())
                .param("password", "invalid")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        response.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("로그인 후 세션을 통해 회원 목록 조회")
    @Test
    void admin_login_after_members() throws Exception {
        MockHttpSession session = new MockHttpSession();

        ResultActions loginResponse = mockMvc.perform(post("/login")
                .param("username", TEST_ADMIN_MEMBER.getEmail())
                .param("password", TEST_ADMIN_MEMBER.getPassword())
                .session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        loginResponse
                .andDo(print())
                .andExpect(status().isOk());

        ResultActions membersResponse = mockMvc.perform(get("/members")
                .session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        membersResponse
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("일반 회원은 회원 목록 조회 불가능")
    @Test
    void user_login_after_members() throws Exception {
        MockHttpSession session = doFormLogin(TEST_USER_MEMBER.getEmail(), TEST_USER_MEMBER.getPassword());

        ResultActions membersResponse = mockMvc.perform(get("/members")
                .session(session)
        );

        membersResponse
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @DisplayName("인증된 사용자는 자신의 정보를 조회할 수 있다.")
    @ParameterizedTest(name = "{1} 사용자")
    @MethodSource("provideMemberUsernamePassword")
    void request_success_members_me(Member member, String role) throws Exception {
        MockHttpSession session = doFormLogin(member.getEmail(), member.getPassword());

        ResultActions response = mockMvc.perform(get("/members/me")
                .session(session)
        );

        response.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("인증되지 않은 사용자는 자신의 정보를 조회할 수 없다.")
    @Test
    void request_fail_members_me() throws Exception {
        ResultActions response = mockMvc.perform(get("/members/me")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        response.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("허용되지 않은 경로는 인증 되었더라도 접근 불가")
    @ParameterizedTest(name = "{1} 사용자")
    @MethodSource("provideMemberUsernamePassword")
    void request_fail_not_allowed_uris(Member member, String role) throws Exception {
        MockHttpSession session = doFormLogin(member.getEmail(), member.getPassword());

        ResultActions response = mockMvc.perform(get("/not-allowed-uri")
                .session(session)
        );

        response.andDo(print())
                .andExpect(status().isForbidden());
    }

    private static Stream<Arguments> provideMemberUsernamePassword() {
        return Stream.of(
                Arguments.of(TEST_ADMIN_MEMBER, "ADMIN"),
                Arguments.of(TEST_USER_MEMBER, "USER")
        );
    }

    private MockHttpSession doFormLogin(String username, String password) throws Exception {
        MockHttpSession session = new MockHttpSession();

        ResultActions loginResponse = mockMvc.perform(post("/login")
                .param("username", username)
                .param("password", password)
                .session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        loginResponse
                .andDo(print())
                .andExpect(status().isOk());

        return session;
    }
}
