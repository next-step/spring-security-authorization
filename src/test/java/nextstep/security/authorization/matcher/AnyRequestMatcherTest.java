package nextstep.security.authorization.matcher;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class AnyRequestMatcherTest {

    @DisplayName("AnyRequestMatcher 는 모든 request 에 대해 match 가 true 이다.")
    @Test
    void matches() {
        assertThat(AnyRequestMatcher.getInstance().matches(
                new MockHttpServletRequest()
        )).isTrue();
    }
}
