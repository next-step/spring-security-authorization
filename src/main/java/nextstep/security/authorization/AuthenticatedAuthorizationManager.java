package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

public class AuthenticatedAuthorizationManager implements AuthorizationManager<Object> {

    @Override
    public AuthorizationDecision check(final Authentication authentication, final Object object) {
        return new AuthorizationDecision(authentication.isAuthenticated());
    }
}
