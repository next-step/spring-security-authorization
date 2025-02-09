package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;

public class AuthenticatedAuthorizationManager<T> implements AuthorizationManager<T> {
    @Override
    public AuthorizationDecision check(Authentication authentication, T target) {
        return AuthorizationDecision.of(
                authentication != null && authentication.isAuthenticated()
        );
    }
}
