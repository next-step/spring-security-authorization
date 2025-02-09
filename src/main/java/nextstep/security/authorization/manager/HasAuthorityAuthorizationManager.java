package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;

import java.util.Set;

public class HasAuthorityAuthorizationManager<T> implements AuthorizationManager<T> {
    private final Set<String> authorities;

    public HasAuthorityAuthorizationManager(String... authorities) {
        this.authorities = Set.of(authorities);
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, T target) {
        return AuthorizationDecision.of(isGranted(authentication));
    }

    private boolean isGranted(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && authentication.getAuthorities().stream()
                .anyMatch(authorities::contains);
    }
}
