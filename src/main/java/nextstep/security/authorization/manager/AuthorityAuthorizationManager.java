package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;
import nextstep.security.role.GrantedAuthority;
import nextstep.security.role.RoleHierarchy;

import java.util.Collection;
import java.util.Set;

public class AuthorityAuthorizationManager<T> implements AuthorizationManager<T> {

    private final RoleHierarchy roleHierarchy;

    public AuthorityAuthorizationManager(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, T authorities) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return AuthorizationDecision.denied();
        }

        Set<GrantedAuthority> userAuthorities = authentication.getAuthorities();
        if (userAuthorities == null || userAuthorities.isEmpty()) {
            return AuthorizationDecision.denied();
        }

        Collection<GrantedAuthority> reachableRoles = roleHierarchy.getReachableRoles(userAuthorities);

        if (authorities instanceof Set<?> requires
                && requires.iterator().next() instanceof GrantedAuthority
        ) {
            @SuppressWarnings("unchecked")
            Set<GrantedAuthority> requiredAuthorities = (Set<GrantedAuthority>) requires;
            for (GrantedAuthority requiredAuthority : requiredAuthorities) {
                if (reachableRoles.contains(requiredAuthority)) {
                    return AuthorizationDecision.granted();
                }
            }

            return AuthorizationDecision.denied();
        }

        return AuthorizationDecision.granted();
    }
}
