package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;

import java.util.List;

public class RequestMatcherDelegatingAuthorizationManager implements AuthorizationManager<HttpServletRequest> {
    private static final String LOGIN_REQUEST_URI = "/login";
    private static final String PRIVATE_REQUEST_URI = "/members/me";
    private static final String ADMIN_REQUEST_URI = "/members";
    private static final String SEARCH_REQUEST_URI = "/search";

    private static final List<String> ALLOW_URI = List.of(LOGIN_REQUEST_URI, PRIVATE_REQUEST_URI, ADMIN_REQUEST_URI, SEARCH_REQUEST_URI);

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest request) {
        final String requestURI = request.getRequestURI();

        if (!ALLOW_URI.contains(requestURI)) {
            return AuthorizationDecision.unAuthorizationDecision();
        }

        if (List.of(SEARCH_REQUEST_URI, LOGIN_REQUEST_URI).contains(requestURI)) {
            return new AuthorizationDecision(true);
        }

        if (authentication == null) {
            return AuthorizationDecision.unAuthorizationDecision();
        }

        if (PRIVATE_REQUEST_URI.equals(requestURI) && authentication.isAuthenticated()) {
            return new AuthorizationDecision(true);
        }

        if (ADMIN_REQUEST_URI.equals(requestURI) && authentication.getAuthorities().contains("ADMIN")) {
            return new AuthorizationDecision(true);
        }

        return AuthorizationDecision.unAuthorizationDecision();
    }
}
