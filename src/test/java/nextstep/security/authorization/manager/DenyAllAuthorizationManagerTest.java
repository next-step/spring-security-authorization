package nextstep.security.authorization.manager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.security.authorization.manager.AuthorizationDecision.NOT_GRANTED;
import static org.assertj.core.api.Assertions.assertThat;

class DenyAllAuthorizationManagerTest {
    private final AuthorizationManager<String> manager = new DenyAllAuthorizationManager<>();

    @DisplayName("DenyAll 일 경우 항상 Granted 되어 있지 않다.")
    @Test
    void check() {
        assertThat(manager.check(null, null))
                .isEqualTo(NOT_GRANTED);
    }
}
