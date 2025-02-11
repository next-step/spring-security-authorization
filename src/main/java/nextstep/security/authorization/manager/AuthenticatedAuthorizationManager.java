package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;

public class AuthenticatedAuthorizationManager<T> implements AuthorizationManager<T> {
    @Override
    public AuthorizationResult authorize(Authentication authentication, T target) {
        return AuthorizationDecision.of(
                authentication != null && authentication.isAuthenticated()
        );
    }
}
