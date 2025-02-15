package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

import java.util.Collection;

public class HasAuthorityAuthorizationManager implements AuthorizationManager {

    private final String authority;
    private final RoleHierarchy roleHierarchy;

    public HasAuthorityAuthorizationManager(String authority) {
        this.authority = authority;
        this.roleHierarchy = new NullRoleHierarchy();
    }

    public HasAuthorityAuthorizationManager(String authority, RoleHierarchy roleHierarchy) {
        this.authority = authority;
        this.roleHierarchy = roleHierarchy;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, Object object) {

        Collection<String> reachableGrantedAuthorities = roleHierarchy.getReachableGrantedAuthorities(authentication.getAuthorities());

        return new AuthorizationDecision(reachableGrantedAuthorities.contains(authority));
    }
}
