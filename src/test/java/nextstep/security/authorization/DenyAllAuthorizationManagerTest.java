package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class DenyAllAuthorizationManagerTest {


    private AuthorizationManager<HttpServletRequest> manager = new DenyAllAuthorizationManager<>();

    @Test
    @DisplayName("어떠한 요청이든, 거부합니다.")
    void deny() {
        //given
        final Authentication authentication = null;
        final MockHttpServletRequest request = new MockHttpServletRequest();

        //when
        final boolean result = manager.check(authentication, request).isAuthorization();

        //then
        assertThat(result).isFalse();
    }
}
