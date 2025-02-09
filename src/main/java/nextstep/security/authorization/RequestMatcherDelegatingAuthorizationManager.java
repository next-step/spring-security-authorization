package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;

import java.util.List;
import java.util.Set;

public class RequestMatcherDelegatingAuthorizationManager implements AuthorizationManager<HttpServletRequest> {
    private static final String DEFAULT_REQUEST_URI = "/members";
    private static final String REQUEST_URI = "/members/me";
    private static final String SEARCH_URI = "/search";

    private static final List<String> uris = List.of(DEFAULT_REQUEST_URI, REQUEST_URI);

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest request) {
        if (DEFAULT_REQUEST_URI.equals(request.getRequestURI())) {
            Set<String> authorities = authentication.getAuthorities();

            if (!authorities.contains("ADMIN")) {
                throw new ForbiddenException();
            }
            return new AuthorizationDecision(true);
        }

        if (REQUEST_URI.equals(request.getRequestURI())) {
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new AuthenticationException();
            }
            return new AuthorizationDecision(true);
        }

        if (SEARCH_URI.equals(request.getRequestURI())) {
            return new AuthorizationDecision(true);
        }

        throw new ForbiddenException();
    }
}
