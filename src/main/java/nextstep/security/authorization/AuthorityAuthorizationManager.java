package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

import java.util.Set;

public class AuthorityAuthorizationManager<T> implements AuthorizationManager<T> {
    private final AuthoritiesAuthorizationManager delegate = new AuthoritiesAuthorizationManager();

    private final String authority;

    public AuthorityAuthorizationManager(String authority) {
        this.authority = authority;
    }

    @Override
    public AuthorizationDecision check(final Authentication authentication, final T object) {
        return this.delegate.check(authentication, Set.of(this.authority));
    }
}
