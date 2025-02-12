package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;

import java.util.Set;

public class AuthorityAuthorizationManager<T> implements AuthorizationManager<T> {
    private final Set<String> authorities;

    public AuthorityAuthorizationManager(String... authorities) {
        this.authorities = Set.of(authorities);
    }

    @Override
    public AuthorizationResult authorize(Authentication authentication, T target) {
        return AuthorizationDecision.of(isGranted(authentication));
    }

    private boolean isGranted(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && anyMatch(authentication);
    }

    private boolean anyMatch(Authentication authentication) {
        for (var authority : authentication.getAuthorities()) {
            if (authorities.contains(authority)) {
                return true;
            }
        }
        return false;
    }
}
