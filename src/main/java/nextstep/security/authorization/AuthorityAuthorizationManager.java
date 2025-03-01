package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;

import java.util.Objects;
import java.util.Set;

public class AuthorityAuthorizationManager implements AuthorizationManager<HttpServletRequest> {
    private RoleHierarchy definedAuthorities = new NullRoleHierarchy();
    private final String allowedRoles;

    public AuthorityAuthorizationManager(RoleHierarchy roleHierarchy, String authority) {
        definedAuthorities = roleHierarchy;
        allowedRoles = authority;
    }

    public AuthorityAuthorizationManager(String authority) {
        allowedRoles = authority;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest object) {
        if (Objects.isNull(authentication)) {
            return new AuthorizationDecision(false);
        }
        Set<String> requestedAuthorities = authentication.getAuthorities();

        if (hasAuthority(requestedAuthorities)) {
            return new AuthorizationDecision(true);

        }
        return new AuthorizationDecision(false);
    }

    public boolean hasAuthority(Set<String> requestedAuthorities) {
        for (String requestedAuthority : requestedAuthorities) {
            Set<String> reachableRoleAuthorities = definedAuthorities.getReachableRoleAuthorities(requestedAuthority);
            boolean contains = reachableRoleAuthorities.contains(allowedRoles);
            if (contains) {
                return true;
            }
        }
        return false;
    }
}
