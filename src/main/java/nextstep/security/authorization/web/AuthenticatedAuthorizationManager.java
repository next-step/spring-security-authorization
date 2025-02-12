package nextstep.security.authorization.web;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;
import nextstep.security.authorization.AuthorizationManager;

public class AuthenticatedAuthorizationManager implements AuthorizationManager<HttpServletRequest> {
    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest object) {
        if (authentication != null && authentication.isAuthenticated()) {
            return AuthorizationDecision.ALLOW;
        }
        return AuthorizationDecision.DENY;
    }
}
