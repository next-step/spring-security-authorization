package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PermitAllAuthorizationManagerTest {

    private final AuthorizationManager<HttpServletRequest> manager = new PermitAllAuthorizationManager();

    @Test
    @DisplayName("인증이 되지 않았더라도, true를 반환합니다.")
    void returnTrue() {
        //given
        final Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated("id", "password");
        final MockHttpServletRequest request = new MockHttpServletRequest();

        //when
        final AuthorizationDecision result = manager.check(authentication, request);

        //then
        assertThat(result.result).isTrue();
    }

    @Test
    @DisplayName("인증 성공했을 경우 상관 없이 true를 반환합니다.")
    void authorization() {
        //given
        final Authentication authentication = UsernamePasswordAuthenticationToken.authenticated("id", "password", Set.of("AAA"));
        final MockHttpServletRequest request = new MockHttpServletRequest();

        //when
        final AuthorizationDecision result = manager.check(authentication, request);

        //then
        assertThat(result.result).isTrue();
    }
}
