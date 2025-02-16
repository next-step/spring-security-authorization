package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorityAuthorizationManagerTest {

    private static final String VALID_ROLE = "ADMIN";

    private final AuthorizationManager<HttpServletRequest> manager = new AuthorityAuthorizationManager(VALID_ROLE);

    @Test
    @DisplayName("authentication가 없으면, false를 반환한다.")
    void isNull() {
        //given
        final Authentication authentication = null;
        final MockHttpServletRequest request = new MockHttpServletRequest();

        //when
        final boolean result = manager.check(authentication, request).isAuthorization();

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("인증되지 않은 요청이 들어오면, false를 반환한다.")
    void failed() {
        //given
        final Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated("id", "password");
        final MockHttpServletRequest request = new MockHttpServletRequest();

        //when
        final boolean result = manager.check(authentication, request).isAuthorization();

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("인증에 성공 했으나, 권한이 다르다면 false를 반환한다.")
    void invalidRole() {
        //given
        final String role = "USER";
        final Authentication authentication = UsernamePasswordAuthenticationToken.authenticated("id", "password", Set.of(role));
        final MockHttpServletRequest request = new MockHttpServletRequest();

        //when
        final boolean result = manager.check(authentication, request).isAuthorization();

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("인증도 성공하며, 권한도 유효하면 true를 반환한다.")
    void validRequest() {
        //given
        final String role = "ADMIN";
        final Authentication authentication = UsernamePasswordAuthenticationToken.authenticated("id", "password", Set.of(role));
        final MockHttpServletRequest request = new MockHttpServletRequest();

        //when
        final boolean result = manager.check(authentication, request).isAuthorization();

        //then
        assertThat(result).isTrue();
    }
}
