package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.fixture.TestAuthentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class HasAuthorityAuthorizationManagerTest {
    private static final String ADMIN_ROLE = "ADMIN";

    @DisplayName("Authenticate가 비어 있으면 인가를 거부한다")
    @Test
    void unAuthorized() {
        HasAuthorityAuthorizationManager authenticatedAuthorizationManager = new HasAuthorityAuthorizationManager(ADMIN_ROLE);

        AuthorizationDecision check = authenticatedAuthorizationManager.check(null, new MockHttpServletRequest());

        assertThat(check.isDeny()).isTrue();
    }

    @DisplayName("인증되어 있지 않으면 인가를 거부한다")
    @Test
    void unAuthenticate() {
        HasAuthorityAuthorizationManager authenticatedAuthorizationManager = new HasAuthorityAuthorizationManager(ADMIN_ROLE);
        Authentication authentication = TestAuthentication.unAuthenticated();

        AuthorizationDecision check = authenticatedAuthorizationManager.check(authentication, new MockHttpServletRequest());

        assertThat(check.isDeny()).isTrue();
    }

    @DisplayName("매칭된 권한이 없으면 인가를 거부한다")
    @Test
    void authenticate() {
        HasAuthorityAuthorizationManager authenticatedAuthorizationManager = new HasAuthorityAuthorizationManager(ADMIN_ROLE);
        Authentication authentication = TestAuthentication.user();

        AuthorizationDecision check = authenticatedAuthorizationManager.check(authentication, new MockHttpServletRequest());

        assertThat(check.isDeny()).isTrue();
    }

    @DisplayName("매칭된 권한이 있으면 인증을 허용한다")
    @Test
    void admin_authenticate() {
        HasAuthorityAuthorizationManager authenticatedAuthorizationManager = new HasAuthorityAuthorizationManager(ADMIN_ROLE);
        Authentication authentication = TestAuthentication.admin();

        AuthorizationDecision check = authenticatedAuthorizationManager.check(authentication, new MockHttpServletRequest());

        assertThat(check.isDeny()).isFalse();
    }
}
