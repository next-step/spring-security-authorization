package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;

public class RequestAuthorizationManager implements AuthorizationManager<HttpServletRequest> {

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest request) {
        if (request.getRequestURI().equals("/members")) {
            if (authentication == null) {
                throw new AuthenticationException();
            }

            boolean isGranted = authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.equals("ADMIN"));

            return new AuthorizationDecision(isGranted);
        }

        if (request.getRequestURI().equals("/members/me")) {
            if (authentication == null) {
                throw new AuthenticationException();
            }

            boolean isGranted = authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.equals("ADMIN") || authority.equals("USER"));

            return new AuthorizationDecision(isGranted);
        }

        if (request.getRequestURI().equals("/search")) {
            return new AuthorizationDecision(true);
        }

        throw new ForbiddenException();
    }
}
