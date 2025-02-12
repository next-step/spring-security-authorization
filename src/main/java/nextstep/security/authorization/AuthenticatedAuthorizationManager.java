package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;

public class AuthenticatedAuthorizationManager implements AuthorizationManager<HttpServletRequest> {
    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest object) {
        if (authentication == null) {
            throw new AuthenticationException();
        }

        if (authentication.isAuthenticated()) {
            return new AuthorizationDecision(true);
        }

        return AuthorizationDecision.unAuthorizationDecision();
    }
}
