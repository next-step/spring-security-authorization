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

class AuthorityAuthorizationManagerTest {
    private final AuthorizationManager<String> manager = new AuthorityAuthorizationManager<>(
            "ADMIN", "USER"
    );

    @DisplayName("인증되지 않는 유저는 인가를 받지 않는다.")
    @Test
    void notAuthenticated() {
        assertAll(
                () -> assertThat(manager.authorize(null, null)).isEqualTo(NOT_GRANTED),
                () -> assertThat(manager.authorize(unauthenticatedUser(), null)).isEqualTo(NOT_GRANTED)
        );
    }

    @DisplayName("어드민 유저는 인가을 받는다.")
    @Test
    void adminWithAuthority() {
        AuthorizationResult admin = manager.authorize(authenticatedUserWithAuthorities("ADMIN"), null);

        assertThat(admin).isEqualTo(GRANTED);
    }

    @DisplayName("일반 유저는 인가를 받는다.")
    @Test
    void userWithAuthority() {
        AuthorizationResult user = manager.authorize(authenticatedUserWithAuthorities("USER"), null);

        assertThat(user).isEqualTo(GRANTED);
    }

    private Authentication authenticatedUserWithAuthorities(String... authorities) {
        return createAuthentication(true, authorities);
    }

    private Authentication unauthenticatedUser() {
        return createAuthentication(false);
    }

    private Authentication createAuthentication(boolean isAuthenticated, String... authorities) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(isAuthenticated);
        when(authentication.getAuthorities()).thenReturn(Set.of(authorities));
        when(authentication.getCredentials()).thenReturn("PASSWORD");
        when(authentication.getPrincipal()).thenReturn("USERNAME");
        return authentication;
    }
}
