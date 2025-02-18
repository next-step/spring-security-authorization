package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

public class DenyAllAuthorizationManager<T> implements AuthorizationManager<T> {
    @Override
    public AuthorizationDecision check(final Authentication authentication, final T object) {
        return new AuthorizationDecision(false);
    }
}
