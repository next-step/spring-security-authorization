package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

import java.util.Set;

public class AuthorityAuthorizationManager<T> implements AuthorizationManager<T> {

    private final Set<String> authorities;

    public AuthorityAuthorizationManager(Set<String> authorities) {
        this.authorities = authorities;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, T object) {

        boolean granted = authentication.getAuthorities()
                .stream()
                .anyMatch(authorities::contains);

        return new AuthorizationDecision(granted);
    }
}
