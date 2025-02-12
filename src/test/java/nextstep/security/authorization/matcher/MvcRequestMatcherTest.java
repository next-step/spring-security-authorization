package nextstep.security.authorization.matcher;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MvcRequestMatcherTest {
    private final MvcRequestMatcher matcher = new MvcRequestMatcher(
            HttpMethod.GET, "/members"
    );

    @DisplayName("method 와 pattern 이 같아야 match 가 true 이다.")
    @Test
    void matches() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/members");
        assertThat(matcher.matches(request)).isTrue();
    }

    @DisplayName("method 가 다르면 match 가 false 이다.")
    @Test
    void differentMethod() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/members");
        assertThat(matcher.matches(request)).isFalse();
    }

    @DisplayName("pattern 이 다르면 match 가 false 이다.")
    @Test
    void differentPattern() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/member");
        assertThat(matcher.matches(request)).isFalse();
    }

    @DisplayName("method 와 pattern 이 다르면 match 가 false 이다.")
    @Test
    void different() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/member");
        assertThat(matcher.matches(request)).isFalse();
    }

    @DisplayName("method 와 pattern 이 존재하지 않으면 match 가 true 이다.")
    @Test
    void nullMethodPattern() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/member");
        assertAll(
                () -> assertThat(new MvcRequestMatcher(
                        null, null
                ).matches(request)).isTrue(),
                () -> assertThat(new MvcRequestMatcher(
                        null, "   "
                ).matches(request)).isTrue()
        );
    }
}
