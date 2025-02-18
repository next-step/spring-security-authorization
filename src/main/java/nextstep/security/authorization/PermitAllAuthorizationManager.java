package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

public class PermitAllAuthorizationManager<T> implements AuthorizationManager<T> {
    @Override
    public AuthorizationDecision check(final Authentication authentication, final T object) {
        return new AuthorizationDecision(true);
    }
}
