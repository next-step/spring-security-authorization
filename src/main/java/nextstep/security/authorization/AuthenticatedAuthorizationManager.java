package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;

public class AuthenticatedAuthorizationManager implements AuthorizationManager<HttpServletRequest> {

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest object) {
        if (authentication == null) {
            return AuthorizationDecision.unAuthorizationDecision();
        }

        if (authentication.isAuthenticated()) {
            return AuthorizationDecision.authorizationDecision();
        }

        return AuthorizationDecision.unAuthorizationDecision();
    }
}
