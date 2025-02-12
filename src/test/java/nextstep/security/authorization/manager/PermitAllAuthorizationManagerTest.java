package nextstep.security.authorization.manager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.security.authorization.manager.AuthorizationDecision.GRANTED;
import static org.assertj.core.api.Assertions.assertThat;

class PermitAllAuthorizationManagerTest {
    private final AuthorizationManager<String> manager = new PermitAllAuthorizationManager<>();

    @DisplayName("PermitAll 일 경우 항상 Granted 되어 있다.")
    @Test
    void authorize() {
        assertThat(manager.authorize(null, null))
                .isEqualTo(GRANTED);
    }
}
