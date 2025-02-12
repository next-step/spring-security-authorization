package nextstep.security.authorization.manager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class AuthorizationDecisionTest {

    @DisplayName("인증 여부(granted) 를 저장할 수 있다.")
    @Test
    void isGranted() {
        assertAll(
                () -> assertThat(
                        AuthorizationDecision.of(true).isGranted()
                ).isTrue(),
                () -> assertThat(
                        AuthorizationDecision.of(false).isGranted()
                ).isFalse()
        );
    }
}
