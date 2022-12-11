package nextstep.app;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.UsernamePasswordAuthentication;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.app.domain.Member;
import nextstep.app.infrastructure.InmemoryMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Base64;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberTest {
    private static final Member TEST_ADMIN_MEMBER = InmemoryMemberRepository.ADMIN_MEMBER;
    private static final Member TEST_USER_MEMBER = InmemoryMemberRepository.USER_MEMBER;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void members_request_success_with_admin_user() throws Exception {
        ResultActions response = requestMembersWithBasicAuthAndSession(
                TEST_ADMIN_MEMBER.getEmail(), TEST_ADMIN_MEMBER.getPassword(), TEST_ADMIN_MEMBER.getRoles()
        );

        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication.isAuthenticated()).isTrue();
        assertThat(authentication.getAuthorities()).contains("ADMIN");
    }

    @Test
    void members_request_fail_with_general_user() throws Exception {
        ResultActions response = requestMembersWithBasicAuthAndSession(
                TEST_USER_MEMBER.getEmail(), TEST_USER_MEMBER.getPassword(), TEST_USER_MEMBER.getRoles()
        );

        response.andExpect(status().isForbidden());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication.isAuthenticated()).isTrue();
        assertThat(authentication.getAuthorities()).isEmpty();
    }

    @Test
    void members_request_fail_with_no_user() throws Exception {
        ResultActions response = requestMembersWithBasicAuthAndSession("none", "none", Collections.emptySet());

        response.andExpect(status().isUnauthorized());
    }

    @Test
    void members_request_fail_invalid_password() throws Exception {
        ResultActions response = requestMembersWithBasicAuthAndSession(TEST_ADMIN_MEMBER.getEmail(), "invalid", Collections.emptySet());

        response.andExpect(status().isUnauthorized());
    }

    @Test
    void members_me_request_success() throws Exception {
        ResultActions response = requestMembersMeWithBasicAuthAndSession(
                TEST_USER_MEMBER.getEmail(), TEST_ADMIN_MEMBER.getPassword(), TEST_ADMIN_MEMBER.getRoles()
        );

        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(TEST_USER_MEMBER.getEmail()));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication.isAuthenticated()).isTrue();
    }

    @Test
    void members_me_request_fail() throws Exception {
        ResultActions response = requestMembersMeWithBasicAuthAndSession(
                TEST_USER_MEMBER.getEmail(), "invalid", Collections.emptySet()
        );

        response.andExpect(status().isUnauthorized());
    }

    private ResultActions requestMembersWithBasicAuthAndSession(String username, String password, Set<String> roles) throws Exception {
        String token = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

        return mockMvc.perform(get("/members")
                        .header("Authorization", "Basic " + token)
                        .session(getLoginSession(username, password, roles))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );
    }

    private ResultActions requestMembersMeWithBasicAuthAndSession(String username, String password, Set<String> roles) throws Exception {
        String token = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

        return mockMvc.perform(get("/members/me")
                .header("Authorization", "Basic " + token)
                .session(getLoginSession(username, password, roles))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );
    }

    private MockHttpSession getLoginSession(String username, String password, Set<String> roles) {
        MockHttpSession session = new MockHttpSession();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(UsernamePasswordAuthentication.ofAuthenticated(username, password, roles));
        session.setAttribute("SPRING_SECURITY_CONTEXT", context);
        return session;
    }
}
