package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Set;

public class AuthoritiesAuthorizationManager implements AuthorizationManager<Collection<String>> {
    private final RoleHierarchy roleHierarchy;

    public AuthoritiesAuthorizationManager(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, Collection<String> authorities) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return AuthorizationDecision.deny();
        }

        if (isHasAccess(authentication, authorities)) {
            return AuthorizationDecision.granted();
        }

        return AuthorizationDecision.deny();
    }

    private boolean isHasAccess(Authentication authentication, Collection<String> authorities) {
        Set<String> authenticationAuthorities = authentication.getAuthorities();

        if (isMatchAuthority(authorities, authenticationAuthorities)) {
            return true;
        }

        Collection<String> reachableGrantedAuthorities = roleHierarchy.getReachableGrantedAuthorities(authentication.getAuthorities());

        return isMatchAuthority(reachableGrantedAuthorities, authenticationAuthorities);
    }

    private static boolean isMatchAuthority(Collection<String> authorities, Set<String> authenticationAuthorities) {
        return CollectionUtils.containsAny(authorities, authenticationAuthorities);
    }
}
