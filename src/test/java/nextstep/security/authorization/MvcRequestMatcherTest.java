package nextstep.security.authorization;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class MvcRequestMatcherTest {

    @Test
    @DisplayName("유효한 HttpMethod와 요청이 서로 다르면 false를 반환한다.")
    void invalidHttpMethod() {
        //given
        final HttpMethod httpMethod = HttpMethod.DELETE;
        final String uri = "/login";

        final RequestMatcher requestMatcher = new MvcRequestMatcher(httpMethod, uri);
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", uri);

        //when
        final boolean result = requestMatcher.matches(request);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("유효한 URI와 요청 URI가 서로 다르면 false를 반환한다.")
    void invalidUri() {
        //given
        final HttpMethod httpMethod = HttpMethod.DELETE;
        final String uri = "/login";

        final RequestMatcher requestMatcher = new MvcRequestMatcher(httpMethod, uri);
        final MockHttpServletRequest request = new MockHttpServletRequest(httpMethod.name(), "/signup");

        //when
        final boolean result = requestMatcher.matches(request);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("유효한 정보가 모두 동일한 경우, true를 반환한다.")
    void validRequest() {
        //given
        final HttpMethod httpMethod = HttpMethod.DELETE;
        final String uri = "/login";

        final RequestMatcher requestMatcher = new MvcRequestMatcher(httpMethod, uri);
        final MockHttpServletRequest request = new MockHttpServletRequest(httpMethod.name(), uri);

        //when
        final boolean result = requestMatcher.matches(request);

        //then
        assertThat(result).isTrue();
    }
}
