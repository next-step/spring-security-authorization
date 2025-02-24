package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

public class AuthenticatedAuthorizationManager<T> implements AuthorizationManager<T> {

    @Override
    public AuthorizationDecision check(Authentication authentication, T object) {
        if (authentication != null && authentication.isAuthenticated()) {
            return AuthorizationDecision.grantedOf();
        }
        return AuthorizationDecision.deniedOf();
    }
}
