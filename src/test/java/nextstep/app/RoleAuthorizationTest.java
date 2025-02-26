package nextstep.app;

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

import static nextstep.app.MemberFixture.TEST_ADMIN_MEMBER;
import static nextstep.app.MemberFixture.TEST_USER_MEMBER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RoleAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.save(TEST_ADMIN_MEMBER);
        memberRepository.save(TEST_USER_MEMBER);
    }

    @DisplayName("로그인 후 어드민은 USER 권한 부여된 /user 에 접근가능하다 ")
    @Test
    void adminHasUserRole() throws Exception {
        MockHttpSession session = new MockHttpSession();

        ResultActions loginResponse = mockMvc.perform(post("/login")
                .param("username", TEST_ADMIN_MEMBER.getEmail())
                .param("password", TEST_ADMIN_MEMBER.getPassword())
                .session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        loginResponse.andExpect(status().isOk());

        ResultActions membersResponse = mockMvc.perform(get("/user")
                .session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        membersResponse.andExpect(status().isOk());
    }


}
