package nextstep.app;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.authorization.Secured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(BasicAuthTest.TestController.class)
@AutoConfigureMockMvc
class BasicAuthTest {
    private final Member TEST_ADMIN_MEMBER = new Member("a@a.com", "password", "a", "", Set.of("ADMIN"));
    private final Member TEST_USER_MEMBER = new Member("b@b.com", "password", "b", "", Set.of("USER"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.save(TEST_ADMIN_MEMBER);
        memberRepository.save(TEST_USER_MEMBER);
    }

    @DisplayName("ADMIN 권한을 가진 사용자가 요청할 경우 모든 회원 정보를 조회할 수 있다.")
    @Test
    void request_success_with_admin_user() throws Exception {
        String token = Base64.getEncoder().encodeToString((TEST_ADMIN_MEMBER.getEmail() + ":" + TEST_ADMIN_MEMBER.getPassword()).getBytes());

        ResultActions response = mockMvc.perform(get("/members")
                .header("Authorization", "Basic " + token)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @DisplayName("일반 사용자가 요청할 경우 권한이 없어야 한다.")
    @Test
    void request_fail_with_general_user() throws Exception {
        String token = Base64.getEncoder().encodeToString((TEST_USER_MEMBER.getEmail() + ":" + TEST_USER_MEMBER.getPassword()).getBytes());

        ResultActions response = mockMvc.perform(get("/members")
                .header("Authorization", "Basic " + token)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        response.andExpect(status().isForbidden());
    }

    @DisplayName("사용자 정보가 없는 경우 요청이 실패해야 한다.")
    @Test
    void request_fail_with_no_user() throws Exception {
        String token = Base64.getEncoder().encodeToString(("none" + ":" + "none").getBytes());

        ResultActions response = mockMvc.perform(get("/members")
                .header("Authorization", "Basic " + token)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        response.andExpect(status().isUnauthorized());
    }

    @DisplayName("Invalid한 패스워드로 요청할 경우 실패해야 한다.")
    @Test
    void request_fail_invalid_password() throws Exception {
        String token = Base64.getEncoder().encodeToString((TEST_ADMIN_MEMBER.getEmail() + ":" + "invalid").getBytes());

        ResultActions response = mockMvc.perform(get("/members")
                .header("Authorization", "Basic " + token)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        response.andExpect(status().isUnauthorized());
    }

    @DisplayName("인증된 사용자는 자신의 정보를 조회할 수 있다.")
    @Test
    void request_success_members_me() throws Exception {
        String token = Base64.getEncoder().encodeToString((TEST_ADMIN_MEMBER.getEmail() + ":" + TEST_ADMIN_MEMBER.getPassword()).getBytes());

        ResultActions response = mockMvc.perform(get("/members/me")
                .header("Authorization", "Basic " + token)
                .param("email", TEST_ADMIN_MEMBER.getEmail())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(TEST_ADMIN_MEMBER.getEmail()));
    }

    @DisplayName("인증되지 않은 사용자는 자신의 정보를 조회할 수 없다.")
    @Test
    void request_fail_members_me() throws Exception {

        ResultActions response = mockMvc.perform(get("/members/me")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        response.andExpect(status().isForbidden());
    }

    @DisplayName("인증이 되었어도, 허가된 요청이 아닌 경우 403 Forbidden을 반환한다.")
    @Test
    void request_fail_other_uri() throws Exception {
        String token = Base64.getEncoder().encodeToString((TEST_ADMIN_MEMBER.getEmail() + ":" + TEST_ADMIN_MEMBER.getPassword()).getBytes());

        ResultActions response = mockMvc.perform(get("/invalid")
                .header("Authorization", "Basic " + token)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        response.andExpect(status().isForbidden());
    }

    @DisplayName("USER 권한을 가진 사용자가 ADMIN 권한 api를 요청할 경우 403 Forbidden을 반환한다.")
    @Test
    void request_fail_with_user_permission() throws Exception {
        String token = Base64.getEncoder().encodeToString((TEST_USER_MEMBER.getEmail() + ":" + TEST_USER_MEMBER.getPassword()).getBytes());

        ResultActions response = mockMvc.perform(get("/admin-granted")
                .header("Authorization", "Basic " + token)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        response.andExpect(status().isForbidden());
    }

    @DisplayName("ADMIN 권한을 가진 사용자가 USER 권한 api를 요청할 경우 정상 동작한다.")
    @Test
    void request_success_with_user_granted_with_admin_permission() throws Exception {
        String token = Base64.getEncoder().encodeToString((TEST_ADMIN_MEMBER.getEmail() + ":" + TEST_ADMIN_MEMBER.getPassword()).getBytes());

        ResultActions response = mockMvc.perform(get("/user-granted")
                .header("Authorization", "Basic " + token)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("userGranted"));
    }

    @RestController
    static class TestController {
        @GetMapping("/invalid")
        public String forbidden() {
            return "forbidden";
        }

        @Secured("USER")
        @GetMapping("/user-granted")
        public String userGranted() {
            return "userGranted";
        }

        @Secured("ADMIN")
        @GetMapping("/admin-granted")
        public String adminGranted() {
            return "adminGranted";
        }
    }
}
