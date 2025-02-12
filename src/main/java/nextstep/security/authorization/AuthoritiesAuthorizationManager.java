package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.hierarchy.NullRoleHierarchy;
import nextstep.security.authorization.hierarchy.RoleHierarchy;

import java.util.Collection;

public class AuthoritiesAuthorizationManager implements AuthorizationManager<Collection<String>> {
    private RoleHierarchy roleHierarchy;
    
    public AuthoritiesAuthorizationManager() {
        this.roleHierarchy = new NullRoleHierarchy();
    }

    public AuthoritiesAuthorizationManager(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }

    @Override
    public AuthorizationDecision check(final Authentication authentication, final Collection<String> authorities) {
        return new AuthorizationDecision(isGranted(authentication, authorities));
    }

    private boolean isGranted(Authentication authentication, Collection<String> authorities) {
        return authentication != null && isAuthorized(authentication, authorities);
    }

    private boolean isAuthorized(Authentication authentication, Collection<String> authorities) {
        for (String grantedAuthority : getAuthorities(authentication)) {
            if (authorities.contains(grantedAuthority)) {
                return true;
            }
        }

        return false;
    }

    private Collection<String> getAuthorities(Authentication authentication) {
        return this.roleHierarchy.getReachableAuthorities(authentication.getAuthorities());
    }
}
