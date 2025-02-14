package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

import java.util.Collection;

public class HasAuthorityAuthorizationManager implements AuthorizationManager {
    private final String allowRole;
    private RoleHierarchy roleHierarchy = new NullRoleHierarchy();

    public HasAuthorityAuthorizationManager(String allowRole) {
        this.allowRole = allowRole;
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

    public HasAuthorityAuthorizationManager withRoleHierarchy(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
        return this;
    }
}
