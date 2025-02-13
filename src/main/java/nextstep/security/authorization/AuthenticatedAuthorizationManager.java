package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;

public class AuthenticatedAuthorizationManager implements AuthorizationManager<HttpServletRequest> {
    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest request) {
        request.setAttribute("username", authentication.getPrincipal());
        return new AuthorizationDecision(authentication.isAuthenticated());
    }
}
