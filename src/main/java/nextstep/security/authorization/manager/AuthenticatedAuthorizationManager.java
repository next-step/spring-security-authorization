package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;

public class AuthenticatedAuthorizationManager<T> implements AuthorizationManager<T> {

    @Override
    public AuthorizationDecision check(Authentication authentication, T object) {
        boolean granted = isGranted(authentication);
        return new AuthorizationDecision(granted);
    }

    private boolean isGranted(Authentication authentication) {
        if (authentication == null) {
            return false;
        }

        return authentication.isAuthenticated();
    }
}
