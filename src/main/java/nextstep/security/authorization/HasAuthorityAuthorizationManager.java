package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

import java.util.Collection;

public class HasAuthorityAuthorizationManager implements AuthorizationManager {
    private final String allowRole;
    private final RoleHierarchy roleHierarchy;

    public HasAuthorityAuthorizationManager(String allowRole) {
        this(allowRole, new NullRoleHierarchy());
    }

    private HasAuthorityAuthorizationManager(String allowRole, RoleHierarchy roleHierarchy) {
        this.allowRole = allowRole;
        this.roleHierarchy = roleHierarchy;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, Object object) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return AuthorizationDecision.deny();
        }

        if (isHasAccess(authentication)) {
            return AuthorizationDecision.granted();
        }

        return AuthorizationDecision.deny();
    }

    private boolean isHasAccess(Authentication authentication) {
        Collection<String> reachableGrantedAuthorities = roleHierarchy.getReachableGrantedAuthorities(authentication.getAuthorities());

        return reachableGrantedAuthorities.contains(allowRole);
    }

    public static HasAuthorityAuthorizationManager withRoleHierarchy(String allowRole, RoleHierarchy roleHierarchy) {
        return new HasAuthorityAuthorizationManager(allowRole, roleHierarchy);
    }
}
