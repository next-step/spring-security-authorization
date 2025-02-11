package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static nextstep.security.authorization.manager.AuthorizationDecision.GRANTED;
import static nextstep.security.authorization.manager.AuthorizationDecision.NOT_GRANTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class AuthenticatedAuthorizationManagerTest {
    private final AuthorizationManager<String> manager = new AuthenticatedAuthorizationManager<>();

    @DisplayName("유저가 인증되지 않았다면 인가받지 못한다.")
    @Test
    void notAuthenticated() {
        assertAll(
                () -> assertThat(manager.authorize(
                        null, null
                )).isEqualTo(NOT_GRANTED),
                () -> assertThat(manager.authorize(
                        createAuthentication(false), null
                )).isEqualTo(NOT_GRANTED)
        );
    }

    @DisplayName("유저가 인증되었다면 인가받는다.")
    @Test
    void authenticated() {
        assertThat(manager.authorize(
                createAuthentication(true), null)
        ).isEqualTo(GRANTED);
    }

    private Authentication createAuthentication(
            boolean isAuthenticated
    ) {
        return new Authentication() {
            @Override
            public Set<String> getAuthorities() {
                return Set.of();
            }

            @Override
            public Object getCredentials() {
                return "PASSWORD";
            }

            @Override
            public Object getPrincipal() {
                return "USERNAME";
            }

            @Override
            public boolean isAuthenticated() {
                return isAuthenticated;
            }
        };
    }
}
