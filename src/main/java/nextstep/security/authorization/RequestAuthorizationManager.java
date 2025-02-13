package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;

import java.util.Set;

public class RequestAuthorizationManager implements AuthorizationManager<HttpServletRequest> {

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest request) {

        Set<String> authorities = authentication.getAuthorities();
        if ("/members".equals(request.getRequestURI())) {
            if (authorities.contains("ADMIN")) {
                return new AuthorizationDecision(true);
            }

            return new AuthorizationDecision(false);
        }

        if ("/members/me".equals(request.getRequestURI())) {
            if (authentication.isAuthenticated()) {
                request.setAttribute("username", authentication.getPrincipal());
                return new AuthorizationDecision(true);
            }

            return new AuthorizationDecision(false);
        }

        return new AuthorizationDecision(true);
    }
}
