package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.fixture.TestAuthentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorityAuthorizationManagerTest {
    private static final String ADMIN_ROLE = "ADMIN";

    @DisplayName("Authenticate가 비어 있으면 인가를 거부한다")
    @Test
    void unAuthorized() {
        AuthorityAuthorizationManager authenticatedAuthorizationManager = AuthorityAuthorizationManager.hasRole(ADMIN_ROLE);

        AuthorizationDecision check = authenticatedAuthorizationManager.check(null, new MockHttpServletRequest());

        assertThat(check.isDeny()).isTrue();
    }

    @DisplayName("인증되어 있지 않으면 인가를 거부한다")
    @Test
    void unAuthenticate() {
        AuthorityAuthorizationManager authenticatedAuthorizationManager = AuthorityAuthorizationManager.hasRole(ADMIN_ROLE);
        Authentication authentication = TestAuthentication.unAuthenticated();

        AuthorizationDecision check = authenticatedAuthorizationManager.check(authentication, new MockHttpServletRequest());

        assertThat(check.isDeny()).isTrue();
    }

    @DisplayName("매칭된 권한이 없으면 인가를 거부한다")
    @Test
    void authenticate() {
        AuthorityAuthorizationManager authenticatedAuthorizationManager = AuthorityAuthorizationManager.hasRole(ADMIN_ROLE);
        Authentication authentication = TestAuthentication.user();

        AuthorizationDecision check = authenticatedAuthorizationManager.check(authentication, new MockHttpServletRequest());

        assertThat(check.isDeny()).isTrue();
    }

    @DisplayName("매칭된 권한이 있으면 인증을 허용한다")
    @Test
    void admin_authenticate() {
        AuthorityAuthorizationManager authenticatedAuthorizationManager = AuthorityAuthorizationManager.hasRole(ADMIN_ROLE);
        Authentication authentication = TestAuthentication.admin();

        AuthorizationDecision check = authenticatedAuthorizationManager.check(authentication, new MockHttpServletRequest());

        assertThat(check.isDeny()).isFalse();
    }

    @DisplayName("roleHierarchy 를 설정하면 상위 권한에서 인가를 허용한다")
    @Test
    void roleHierarchy_authenticate() {
        // given
        final String allowedRole = "ADMIN";
        RoleHierarchyImpl roleHierarchy = RoleHierarchyImpl.with()
                .role("ADMIN")
                .implies("USER")
                .build();

        AuthorityAuthorizationManager authenticatedAuthorizationManager = AuthorityAuthorizationManager.hasRole(allowedRole, roleHierarchy);


        Authentication authentication = TestAuthentication.admin();


        // when
        AuthorizationDecision check = authenticatedAuthorizationManager.check(authentication, new MockHttpServletRequest());

        // then
        assertThat(check.isDeny()).isFalse();
    }

    @DisplayName("roleHierarchy 를 설정하면 같은 권한에 인가를 허용한다")
    @Test
    void roleHierarchy_equal_authenticate() {
        // given
        final String allowedRole = "USER";
        RoleHierarchyImpl roleHierarchy = RoleHierarchyImpl.with()
                .role("ADMIN")
                .implies("USER")
                .build();

        AuthorityAuthorizationManager authenticatedAuthorizationManager = AuthorityAuthorizationManager.hasRole(allowedRole, roleHierarchy);

        Authentication authentication = TestAuthentication.user();

        // when
        AuthorizationDecision check = authenticatedAuthorizationManager.check(authentication, new MockHttpServletRequest());

        // then
        assertThat(check.isDeny()).isFalse();
    }

    @DisplayName("hasAnyRole 은 여러개의 권한에 대해서 인가처리를 한다")
    @Test
    void hasAnyRole() {
        // given
        AuthorityAuthorizationManager authenticatedAuthorizationManager = AuthorityAuthorizationManager.hasAnyRole("USER","ADMIN");

        // when
        AuthorizationDecision userCheck = authenticatedAuthorizationManager.check(TestAuthentication.user(), new MockHttpServletRequest());
        AuthorizationDecision adminCheck = authenticatedAuthorizationManager.check(TestAuthentication.admin(), new MockHttpServletRequest());

        // then
        assertThat(userCheck.isDeny()).isFalse();
        assertThat(adminCheck.isDeny()).isFalse();
    }
}
