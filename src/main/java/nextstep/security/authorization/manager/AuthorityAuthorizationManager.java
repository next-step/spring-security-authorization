package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;
import nextstep.security.authorization.ForbiddenException;

import java.util.Collection;
import java.util.Set;

public class AuthorityAuthorizationManager<T> implements AuthorizationManager<T> {

    @Override
    public AuthorizationDecision check(Authentication authentication, T authorities) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return AuthorizationDecision.denied();
        }

        boolean hasNotRole = false;

        if (authorities instanceof Collection<?> collection) {
            hasNotRole = roleCheckByCollection(authentication, collection);
        } else if (authorities instanceof String authority) {
            hasNotRole = roleCheckByString(authentication, authority);
        }

        if (hasNotRole) {
            throw new ForbiddenException();
        }

        return AuthorizationDecision.granted();
    }

    private boolean roleCheckByCollection(Authentication authentication, Collection<?> requiredAuthorities) {
        if (requiredAuthorities == null || requiredAuthorities.isEmpty()) {
            return false;
        }

        Set<String> userAuthorities = authentication.getAuthorities();
        if (userAuthorities == null || userAuthorities.isEmpty()) {
            return true;
        }

        if (requiredAuthorities.iterator().next() instanceof String) {

            @SuppressWarnings("unchecked")
            Collection<String> requires = (Collection<String>) requiredAuthorities;
            for (String requiredAuthority : requires) {
                if (userAuthorities.contains(requiredAuthority)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean roleCheckByString(Authentication authentication, String requiredAuthority) {
        Set<String> userAuthorities = authentication.getAuthorities();
        if (userAuthorities == null || userAuthorities.isEmpty()) {
            return true;
        }

        return !userAuthorities.contains(requiredAuthority);
    }
}
