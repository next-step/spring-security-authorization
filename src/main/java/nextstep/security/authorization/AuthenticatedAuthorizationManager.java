package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;

public class AuthenticatedAuthorizationManager implements AuthorizationManager<HttpServletRequest> {
    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest object) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException();
        }
        return new AuthorizationDecision(true);
    }
}
