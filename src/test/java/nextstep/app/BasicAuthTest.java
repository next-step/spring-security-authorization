package nextstep.app;

import nextstep.Steps;
import nextstep.app.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static nextstep.Fixture.TEST_ADMIN_MEMBER;
import static nextstep.Fixture.TEST_ADMIN_TOKEN;
import static nextstep.Fixture.TEST_USER_MEMBER;
import static nextstep.Fixture.TEST_USER_TOKEN;
import static nextstep.Fixture.createToken;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BasicAuthTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        Steps.setUp(memberRepository);
    }

    @DisplayName("ADMIN 권한을 가진 사용자가 요청할 경우 모든 회원 정보를 조회할 수 있다.")
    @Test
    void request_success_with_admin_user() throws Exception {
        mockMvc.perform(
                get("/members")
                        .header("Authorization", TEST_ADMIN_TOKEN)
        ).andExpect(
                status().isOk()
        ).andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @DisplayName("일반 사용자가 요청할 경우 권한이 없어야 한다.")
    @Test
    void request_fail_with_general_user() throws Exception {
        mockMvc.perform(
                get("/members")
                        .header("Authorization", TEST_USER_TOKEN)
        ).andExpect(status().isForbidden());
    }

    @DisplayName("사용자 정보가 없는 경우 요청이 실패해야 한다.")
    @Test
    void request_fail_with_no_user() throws Exception {
        mockMvc.perform(
                get("/members")
                        .header("Authorization", createToken("none", "none"))
        ).andExpect(status().isUnauthorized());
    }

    @DisplayName("Invalid한 패스워드로 요청할 경우 실패해야 한다.")
    @Test
    void request_fail_invalid_password() throws Exception {
        String invalidToken = createToken(TEST_ADMIN_MEMBER.getEmail(), "invalid");

        mockMvc.perform(
                get("/members")
                        .header("Authorization", invalidToken)
        ).andExpect(status().isUnauthorized());
    }

    @DisplayName("인증없이 자신의 정보를 조회하면 실패해야 한다.")
    @Test
    void request_fail_me() throws Exception {
        mockMvc.perform(
                get("/members/me")
        ).andExpect(status().isUnauthorized());
    }

    @DisplayName("일반 사용자는 인증 후 자신의 정보를 조회할 수 있다.")
    @Test
    void request_success_me_with_member() throws Exception {
        mockMvc.perform(
                get("/members/me")
                        .header("Authorization", TEST_USER_TOKEN)
        ).andExpect(
                status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email")
                        .value(TEST_USER_MEMBER.getEmail())
        );

        mockMvc.perform(
                get("/members/me")
                        .header("Authorization", TEST_ADMIN_TOKEN)
        ).andExpect(
                status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email")
                        .value(TEST_ADMIN_MEMBER.getEmail())
        );
    }

    @DisplayName("일반 사용자는 인증 후 자신의 정보를 조회할 수 있다.")
    @Test
    void request_success_me_with_admin() throws Exception {
        mockMvc.perform(
                get("/members/me")
                        .header("Authorization", TEST_ADMIN_TOKEN)
        ).andExpect(
                status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email")
                        .value(TEST_ADMIN_MEMBER.getEmail())
        );
    }
}
