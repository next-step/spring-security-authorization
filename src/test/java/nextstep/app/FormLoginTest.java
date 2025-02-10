package nextstep.app;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FormLoginTest {
    private static final String USER_ROLE = "USER";
    private static final String ADMIN_ROLE = "ADMIN";

    private final Member TEST_ADMIN_MEMBER = new Member("a@a.com", "password", "a", "", Set.of(ADMIN_ROLE));
    private final Member TEST_USER_MEMBER = new Member("b@b.com", "password", "b", "", Set.of(USER_ROLE));

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

        loginResponse.andExpect(status().isOk());
    }

    @DisplayName("로그인 실패 - 사용자 없음")
    @Test
    void login_fail_with_no_user() throws Exception {
        ResultActions response = mockMvc.perform(post("/login")
                .param("username", "none")
                .param("password", "none")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        response.andExpect(status().isUnauthorized());
    }

    @DisplayName("로그인 실패 - 비밀번호 불일치")
    @Test
    void login_fail_with_invalid_password() throws Exception {
        ResultActions response = mockMvc.perform(post("/login")
                .param("username", TEST_USER_MEMBER.getEmail())
                .param("password", "invalid")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        response.andExpect(status().isUnauthorized());
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

        loginResponse.andExpect(status().isOk());

        ResultActions membersResponse = mockMvc.perform(get("/members")
                .session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        membersResponse.andExpect(status().isOk());
    }

    @DisplayName("일반 회원은 회원 목록 조회 불가능")
    @Test
    void user_login_after_members() throws Exception {
        MockHttpSession session = new MockHttpSession();

        ResultActions loginResponse = mockMvc.perform(post("/login")
                .param("username", TEST_USER_MEMBER.getEmail())
                .param("password", TEST_USER_MEMBER.getPassword())
                .session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        loginResponse.andExpect(status().isOk());

        ResultActions membersResponse = mockMvc.perform(get("/members")
                .session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        membersResponse.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("인증된 사용자는 자신의 정보를 조회할 수 있다.")
    void request_success_members_me() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/login")
                .param("username", TEST_USER_MEMBER.getEmail())
                .param("password", TEST_USER_MEMBER.getPassword())
                .session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        ).andExpect(status().isOk());

        mockMvc.perform(get("/members/me")
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(TEST_USER_MEMBER.getEmail()))
                .andExpect(jsonPath("$.password").value(TEST_USER_MEMBER.getPassword()))
                .andExpect(jsonPath("$.name").value(TEST_USER_MEMBER.getName()))
                .andExpect(jsonPath("$.roles").value(USER_ROLE))
                .andExpect(jsonPath("$.imageUrl").value(TEST_USER_MEMBER.getImageUrl()));
    }

    @Test
    @DisplayName("인증되지 않은 사용자는 자신의 정보를 조회할 수 없다.")
    void request_fail_members_me() throws Exception {
        mockMvc.perform(get("/members/me")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        ).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("ADMIN 권한을 가진 사용자가 요청할 경우 모든 회원 정보를 조회할 수 있다.")
    void request_search_success_with_admin_user() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/login")
                .param("username", TEST_ADMIN_MEMBER.getEmail())
                .param("password", TEST_ADMIN_MEMBER.getPassword())
                .session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        ).andExpect(status().isOk());

        mockMvc.perform(get("/members")
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("제한된 URI은 ADMIN 이여도 접근할수 없다")
    void request_private_with_admin_user() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/login")
                .param("username", TEST_ADMIN_MEMBER.getEmail())
                .param("password", TEST_ADMIN_MEMBER.getPassword())
                .session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        ).andExpect(status().isOk());

        mockMvc.perform(post("/private")
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("제한된 URI은 접근할수 없다")
    void request_private_with_user_user() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/login")
                .param("username", TEST_USER_MEMBER.getEmail())
                .param("password", TEST_USER_MEMBER.getPassword())
                .session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        ).andExpect(status().isOk());

        mockMvc.perform(post("/private")
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andExpect(status().isForbidden());
    }
}
