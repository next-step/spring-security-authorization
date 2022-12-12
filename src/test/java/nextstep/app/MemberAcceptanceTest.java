package nextstep.app;

import nextstep.app.domain.Member;
import nextstep.app.infrastructure.InMemoryMemberRepository;
import nextstep.app.ui.dto.MemberDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends AcceptanceTest {
    private static final Member TEST_MEMBER = InMemoryMemberRepository.ADMIN_MEMBER;

    @Test
    void get_members_after_form_login() {
        final var params = new HashMap<String, String>();
        params.put("username", TEST_MEMBER.getEmail());
        params.put("password", TEST_MEMBER.getPassword());

        final var loginResponse = post("/login", params);
        final var memberResponse = get("/members", loginResponse.cookies());

        assertThat(memberResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        final var members = memberResponse.jsonPath().getList(".", MemberDto.class);
        assertThat(members).hasSize(2);
    }
}
