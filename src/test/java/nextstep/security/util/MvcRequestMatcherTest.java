package nextstep.security.util;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;

@DisplayName("MvcRequestMatcher 테스트")
class MvcRequestMatcherTest {

    @DisplayName("matches() - http 메서드와 uri가 같으면 true 반환")
    @Test
    void MatchesWhenMethodAndPatternMatch() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/test");

        MvcRequestMatcher matcher = new MvcRequestMatcher(HttpMethod.GET, "/test");

        // when
        boolean matches = matcher.matches(request);

        // then
        assertThat(matches).isTrue();
    }

    @DisplayName("matches() - http 메서드와 uri중 하나라도 다르면 false 반환")
    @Test
    void MatchesWhenMethodAndPatternIsNotMatch() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/test");

        MvcRequestMatcher matcher = new MvcRequestMatcher(HttpMethod.GET, "/test");

        // when
        boolean matches = matcher.matches(request);

        // then
        assertThat(matches).isFalse();
    }

}