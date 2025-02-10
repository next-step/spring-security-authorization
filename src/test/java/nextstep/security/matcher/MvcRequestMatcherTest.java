package nextstep.security.matcher;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MvcRequestMatcherTest {

    @DisplayName("MvcRequestMatcher 는 httpMethod 를 검증한다")
    @ParameterizedTest
    @ValueSource(strings = {"DELETE", "POST", "PATCH","PUT"})
    void httpMethod(String ignoreHttpMethod)  {
        // given
        final String url = "/users";
        final HttpMethod allowHttpMethod = HttpMethod.GET;

        MockHttpServletRequest allowRequest = new MockHttpServletRequest(allowHttpMethod.toString(), url);
        MockHttpServletRequest ignoreRequest = new MockHttpServletRequest(ignoreHttpMethod, url);

        // when
        MvcRequestMatcher matcher = new MvcRequestMatcher(allowHttpMethod, url);

        // then
        assertSoftly(it -> {
            it.assertThat(matcher.matches(allowRequest)).isTrue();
            it.assertThat(matcher.matches(ignoreRequest)).isFalse();
        });
    }

    @DisplayName("MvcRequestMatcher 는 httpMethod 를 uri을 검증한다")
    @Test
    void uri() {
        // given
        final String allowUrl = "/users";
        final HttpMethod httpMethod = HttpMethod.GET;

        MockHttpServletRequest allowRequest = new MockHttpServletRequest(httpMethod.toString(), allowUrl);
        MockHttpServletRequest ignoreRequest = new MockHttpServletRequest(httpMethod.toString(), "/ignoreUrl");

        // when
        MvcRequestMatcher matcher = new MvcRequestMatcher(httpMethod, allowUrl);

        // then
        assertSoftly(it -> {
            it.assertThat(matcher.matches(allowRequest)).isTrue();
            it.assertThat(matcher.matches(ignoreRequest)).isFalse();
        });
    }

    @DisplayName("MvcRequestMatcher 는 pattern url을 검증한다")
    @Test
    void patternUrl() {
        final String allowUrl = "/users/**";
        final HttpMethod httpMethod = HttpMethod.GET;

        MvcRequestMatcher matcher = new MvcRequestMatcher(httpMethod, allowUrl);

        assertSoftly(it -> {
            it.assertThat(matcher.matches(new MockHttpServletRequest(httpMethod.toString(), "/users"))).isTrue();
            it.assertThat(matcher.matches(new MockHttpServletRequest(httpMethod.toString(), "/users/a"))).isTrue();
            it.assertThat(matcher.matches(new MockHttpServletRequest(httpMethod.toString(), "/users/b/a"))).isTrue();
            it.assertThat(matcher.matches(new MockHttpServletRequest(httpMethod.toString(), "/user"))).isFalse();
        });
    }


}
