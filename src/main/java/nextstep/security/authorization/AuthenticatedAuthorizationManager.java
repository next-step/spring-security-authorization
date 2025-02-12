package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

public class AuthenticatedAuthorizationManager<T> implements AuthorizationManager<T> {

    private final AuthorizationStrategy authorizationStrategy;

    public AuthenticatedAuthorizationManager(AuthorizationStrategy authorizationStrategy) {
        this.authorizationStrategy = authorizationStrategy;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, T object) {
        var granted = authorizationStrategy.isGranted(authentication);
        return new AuthorizationDecision(granted);
    }
}
