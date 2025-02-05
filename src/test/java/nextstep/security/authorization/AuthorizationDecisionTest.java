package nextstep.security.authorization;

import nextstep.security.authorization.manager.AuthorizationDecision;
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
                        AuthorizationDecision.GRANTED.isGranted()
                ).isTrue(),
                () -> assertThat(
                        AuthorizationDecision.NOT_GRANTED.isGranted()
                ).isFalse()
        );
    }
}
