package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

public class AuthenticatedAuthorizationManager<T> implements AuthorizationManager<T> {

    @Override
    public AuthorizationDecision check(final Authentication authentication, final T object) {
        return new AuthorizationDecision(authentication != null && authentication.isAuthenticated());
    }
}
