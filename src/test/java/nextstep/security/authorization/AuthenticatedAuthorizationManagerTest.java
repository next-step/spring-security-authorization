package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.fixture.TestAuthentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticatedAuthorizationManagerTest {

    @DisplayName("Authenticate가 비어 있으면 인가를 거부한다")
    @Test
    void authenticate_empty() {
        AuthenticatedAuthorizationManager authenticatedAuthorizationManager = new AuthenticatedAuthorizationManager();

        AuthorizationDecision check = authenticatedAuthorizationManager.check(null, new MockHttpServletRequest());

        assertThat(check.isDeny()).isTrue();
    }

    @DisplayName("인증되어 있지 않으면 인가를 거부한다")
    @Test
    void unAuthenticate() {
        AuthenticatedAuthorizationManager authenticatedAuthorizationManager = new AuthenticatedAuthorizationManager();
        Authentication authentication = TestAuthentication.unAuthenticated();

        AuthorizationDecision check = authenticatedAuthorizationManager.check(authentication, new MockHttpServletRequest());

        assertThat(check.isDeny()).isTrue();
    }

    @DisplayName("인증되어 있으면 인가를 허용한다")
    @Test
    void authenticate() {
        AuthenticatedAuthorizationManager authenticatedAuthorizationManager = new AuthenticatedAuthorizationManager();
        Authentication authentication = TestAuthentication.authenticated();

        AuthorizationDecision check = authenticatedAuthorizationManager.check(authentication, new MockHttpServletRequest());

        assertThat(check.isDeny()).isFalse();
    }
}
