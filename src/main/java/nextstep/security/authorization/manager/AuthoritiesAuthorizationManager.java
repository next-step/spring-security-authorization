package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;
import nextstep.security.authorization.GrantedAuthority;
import nextstep.security.authorization.role.NullRoleHierarchy;
import nextstep.security.authorization.role.RoleHierarchy;

import java.util.Collection;

public class AuthoritiesAuthorizationManager implements AuthorizationManager<Collection<String>> {

    private final RoleHierarchy roleHierarchy;

    public AuthoritiesAuthorizationManager() {
        this.roleHierarchy = new NullRoleHierarchy();
    }

    public AuthoritiesAuthorizationManager(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, Collection<String> authorities) {

        boolean granted = isGranted(authentication, authorities);
        return new AuthorizationDecision(granted);
    }

    private boolean isGranted(Authentication authentication, Collection<String> authorities) {
        return (authentication != null) && hasAuthority(authentication, authorities);
    }

    private boolean hasAuthority(Authentication authentication, Collection<String> authorities) {
        Collection<GrantedAuthority> reachableAuthorities = getReachableAuthorities(authentication);
        for (GrantedAuthority authority : reachableAuthorities) {
            if (authorities.contains(authority.getAuthority())) {
                return true;
            }
        }

        return false;
    }

    private Collection<GrantedAuthority> getReachableAuthorities(Authentication authentication) {
        return roleHierarchy.getReachableGrantedAuthorities(authentication.getAuthorities());
    }
}
