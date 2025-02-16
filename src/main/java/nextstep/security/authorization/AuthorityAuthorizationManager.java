package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

import java.util.Set;

public class AuthorityAuthorizationManager implements AuthorizationManager {
    private final static NullRoleHierarchy NULL_ROLE_HIERARCHY = new NullRoleHierarchy();
    private final Set<String> allowRoles;
    private final AuthoritiesAuthorizationManager authoritiesAuthorizationManager;

    private AuthorityAuthorizationManager(Set<String> allowRoles, RoleHierarchy roleHierarchy) {
        this.allowRoles = allowRoles;
        this.authoritiesAuthorizationManager = new AuthoritiesAuthorizationManager(roleHierarchy);
    }


    public static AuthorityAuthorizationManager hasRole(String role) {
        return new AuthorityAuthorizationManager(Set.of(role), NULL_ROLE_HIERARCHY);
    }

    public static AuthorityAuthorizationManager hasRole(String role, RoleHierarchy roleHierarchy) {
        return new AuthorityAuthorizationManager(Set.of(role), roleHierarchy);
    }

    public static AuthorityAuthorizationManager hasAnyRole(RoleHierarchy roleHierarchy, String... roles) {
        return new AuthorityAuthorizationManager(Set.of(roles), roleHierarchy);
    }

    public static AuthorityAuthorizationManager hasAnyRole(String... roles) {
        return new AuthorityAuthorizationManager(Set.of(roles), NULL_ROLE_HIERARCHY);
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, Object object) {
        return authoritiesAuthorizationManager.check(authentication, allowRoles);
    }
}
