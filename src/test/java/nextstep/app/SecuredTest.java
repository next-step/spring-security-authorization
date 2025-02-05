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

import static nextstep.Fixture.TEST_ADMIN_TOKEN;
import static nextstep.Fixture.TEST_USER_TOKEN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecuredTest {
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
    void request_search_success_with_admin_user() throws Exception {
        mockMvc.perform(
                get("/search")
                        .header("Authorization", TEST_ADMIN_TOKEN)
        ).andDo(print()).andExpect(
                status().isOk()
        ).andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @DisplayName("일반 사용자가 요청할 경우 권한이 없어야 한다.")
    @Test
    void request_search_fail_with_general_user() throws Exception {
        mockMvc.perform(
                get("/search")
                        .header("Authorization", TEST_USER_TOKEN)
        ).andDo(print()).andExpect(status().isForbidden());
    }
}
