package nextstep.security.authorization.manager;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;

public class AuthenticatedAuthorizationManager implements AuthorizationManager<HttpServletRequest> {

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest object) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return AuthorizationDecision.denied();
        }

        return AuthorizationDecision.granted();
    }
}
