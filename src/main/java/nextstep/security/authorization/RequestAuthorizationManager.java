package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;

import java.util.Set;

public class RequestAuthorizationManager implements AuthorizationManager<HttpServletRequest> {
    @Override
    public AuthorizationDecision check(final Authentication authentication, final HttpServletRequest request) {
        if (request.getRequestURI().equals("/members")) {
            if (!isAuthenticated(authentication)) {
                throw new AuthenticationException();
            }

            return new AuthorizationDecision(isGranted(authentication, "ADMIN"));
        }

        if (request.getRequestURI().equals("/members/me")) {
            if (!isAuthenticated(authentication)) {
                throw new AuthenticationException();
            }

            return new AuthorizationDecision(true);
        }

        if (request.getRequestURI().equals("/search")) {
            return new AuthorizationDecision(true);
        }

        return new AuthorizationDecision(false);
    }


    private boolean isGranted(final Authentication authentication, final String authoritiesURI) {
        final Set<String> authorities = authentication.getAuthorities();
        return authorities.contains(authoritiesURI);
    }

    private boolean isAuthenticated(final Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return true;
    }
}
