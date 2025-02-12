package nextstep.security.matcher;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;


class AnyRequestMatcherTest {

    @DisplayName("어떠한 요청이 오든 허용 한다")
    @Test
    void match() {
        AnyRequestMatcher matcher = new AnyRequestMatcher();

        boolean result = matcher.matches(new MockHttpServletRequest());

        assertThat(result).isTrue();
    }
}
