package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static nextstep.security.authorization.manager.AuthorizationDecision.GRANTED;
import static nextstep.security.authorization.manager.AuthorizationDecision.NOT_GRANTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticatedAuthorizationManagerTest {
    private final AuthorizationManager<String> manager = new AuthenticatedAuthorizationManager<>();

    @DisplayName("유저가 인증되지 않으면 인가되지 않은 상태이다.")
    @Test
    void notAuthenticated() {
        assertAll(
                () -> assertThat(manager.authorize(null, null)).isEqualTo(NOT_GRANTED),
                () -> assertThat(manager.authorize(unauthenticatedUser(), null)).isEqualTo(NOT_GRANTED)
        );
    }

    @DisplayName("유저가 인증되면 인가된 상태이다.")
    @Test
    void authenticated() {
        assertThat(manager.authorize(authenticatedUser(), null)).isEqualTo(GRANTED);
    }

    private Authentication authenticatedUser() {
        return createAuthentication(true);
    }

    private Authentication unauthenticatedUser() {
        return createAuthentication(false);
    }

    private Authentication createAuthentication(boolean isAuthenticated) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(isAuthenticated);
        when(authentication.getAuthorities()).thenReturn(Set.of());
        when(authentication.getCredentials()).thenReturn("PASSWORD");
        when(authentication.getPrincipal()).thenReturn("USERNAME");
        return authentication;
    }
}
