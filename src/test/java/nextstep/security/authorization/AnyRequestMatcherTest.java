package nextstep.security.authorization;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class AnyRequestMatcherTest {

    private final RequestMatcher requestMatcher = new AnyRequestMatcher();

    @Test
    @DisplayName("모든 요청에 대해서 true를 반환한다.")
    void anyRequest() {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();

        //when
        final boolean result = requestMatcher.matches(request);

        //then
        assertThat(result).isTrue();
    }
}
