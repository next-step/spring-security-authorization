package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthenticatedAuthorizationManagerTest {

    private final AuthorizationManager<HttpServletRequest> manager = new AuthenticatedAuthorizationManager();

    @Test
    @DisplayName("authentication가 없으면, 오류가 발생한다.")
    void isNull() {
        //given
        final Authentication authentication = null;
        final MockHttpServletRequest request = new MockHttpServletRequest();

        //when & then
        assertThrows(AuthenticationException.class, () -> manager.check(authentication, request));
    }

    @Test
    @DisplayName("인증에 성공했다면, true를 반환한다.")
    void success() {
        //given
        final Authentication authentication = UsernamePasswordAuthenticationToken.authenticated("id", "password", Set.of("USER"));
        final MockHttpServletRequest request = new MockHttpServletRequest();

        //when
        final AuthorizationDecision result = manager.check(authentication, request);

        //then
        assertThat(result.result).isTrue();
    }

    @Test
    @DisplayName("인증되지 않은 요청이 들어오면, false를 반환한다.")
    void failed() {
        //given
        final Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated("id", "password");
        final MockHttpServletRequest request = new MockHttpServletRequest();

        //when
        final AuthorizationDecision result = manager.check(authentication, request);

        //then
        assertThat(result.result).isFalse();
    }
}
