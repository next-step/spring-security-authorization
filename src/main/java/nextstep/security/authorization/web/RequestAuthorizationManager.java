package nextstep.security.authorization.web;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;
import nextstep.security.authorization.AuthorizationManager;

public class RequestAuthorizationManager implements AuthorizationManager<HttpServletRequest> {
    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest httpRequest) {
        if (httpRequest.getRequestURI().equals("/members")) {
            if (authentication == null) {
                return new AuthorizationDecision(false);
            }

            return new AuthorizationDecision(
                    authentication.getAuthorities().stream()
                            .anyMatch(authority -> authority.equals("ADMIN")));
        }

        if (httpRequest.getRequestURI().equals("/members/me")) {
            if (authentication == null) {
                return new AuthorizationDecision(false);
            }

            return new AuthorizationDecision(authentication.isAuthenticated());
        }

        if (httpRequest.getRequestURI().equals("/search")) {
            return new AuthorizationDecision(true);
        }

        return new AuthorizationDecision(false);
    }
}
