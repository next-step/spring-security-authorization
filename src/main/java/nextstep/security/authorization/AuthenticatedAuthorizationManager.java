package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

public class AuthenticatedAuthorizationManager implements AuthorizationManager {
    @Override
    public AuthorizationDecision check(Authentication authentication, Object object) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return AuthorizationDecision.deny();
        }

        return AuthorizationDecision.granted();
    }
}
