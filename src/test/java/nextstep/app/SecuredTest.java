package nextstep.app;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.infrastructure.InmemoryMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Base64;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecuredTest {
    private final Member TEST_ADMIN_MEMBER = new Member("a@a.com", "password", "a", "", Set.of("ADMIN"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void clear() {
        memberRepository.save(TEST_ADMIN_MEMBER);
    }

    @DisplayName("ADMIN 권한을 가진 사용자가 요청할 경우 모든 회원 정보를 조회할 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"ADMIN", "MEMBER"})
    void request_search_success_with_admin_user(String role) throws Exception {
        Member member = new Member("b@b.com", "password", "b", "", Set.of(role));
        memberRepository.save(member);
        String token = Base64.getEncoder().encodeToString((member.getEmail() + ":" + member.getPassword()).getBytes());

        ResultActions response = mockMvc.perform(get("/search")
                .header("Authorization", "Basic " + token)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        ).andDo(print());

        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @DisplayName("ADMIN 권한을 가진 사용자가 /members를 요청할 경우 모든 회원 정보를 조회할 수 있다.")
    @Test
    void request_members_success_with_admin_user() throws Exception {
        Member member = new Member("b@b.com", "password", "b", "", Set.of("ADMIN"));
        memberRepository.save(member);
        String token = Base64.getEncoder().encodeToString((member.getEmail() + ":" + member.getPassword()).getBytes());

        ResultActions response = mockMvc.perform(get("/members")
                .header("Authorization", "Basic " + token)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        ).andDo(print());

        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @DisplayName("일반 사용자가 /members를 요청할 경우 권한이 없어야 한다.")
    @Test
    void request_members_fail_with_general_user() throws Exception {
        Member member = new Member("b@b.com", "password", "b", "", Set.of("MEMBER"));
        memberRepository.save(member);
        String token = Base64.getEncoder().encodeToString((member.getEmail() + ":" + member.getPassword()).getBytes());

        ResultActions response = mockMvc.perform(get("/members")
                .header("Authorization", "Basic " + token)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        ).andDo(print());

        response.andExpect(status().isForbidden());
    }
}
