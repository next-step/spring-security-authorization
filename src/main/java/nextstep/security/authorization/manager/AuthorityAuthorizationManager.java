package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.role.GrantedAuthority;
import nextstep.security.authorization.role.RoleHierarchy;

import java.util.Collection;

public class AuthorityAuthorizationManager<T> implements AuthorizationManager<T> {
    private final RoleHierarchy roleHierarchy;
    private final String authority;

    public AuthorityAuthorizationManager(RoleHierarchy roleHierarchy, String authority) {
        this.roleHierarchy = roleHierarchy;
        this.authority = authority;
    }

    @Override
    public AuthorizationResult authorize(Authentication authentication, T target) {
        return AuthorizationDecision.of(isGranted(authentication));
    }

    private boolean isGranted(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && isAuthorized(authentication);
    }

    private boolean isAuthorized(Authentication authentication) {
        final Collection<GrantedAuthority> authorities = roleHierarchy
                .getReachableGrantedAuthorities(
                        GrantedAuthority.setOf(authentication.getAuthorities())
                );
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals(authority)) {
                return true;
            }
        }
        return false;
    }
}
