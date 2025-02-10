package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;

import java.util.List;

public class RequestAuthorizationManager implements AuthorizationManager<HttpServletRequest> {

    private static final String LOGIN_REQUEST_URI = "/login";
    private static final String PRIVATE_REQUEST_URI = "/members/me";
    private static final String ADMIN_REQUEST_URI = "/members";

    private static final String ALL_ALLOW_URI = "/search";
    private List<String> allowUri = List.of(LOGIN_REQUEST_URI, PRIVATE_REQUEST_URI, ADMIN_REQUEST_URI, ALL_ALLOW_URI);

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest request) {
        final String requestURI = request.getRequestURI();

        if (ALL_ALLOW_URI.equals(requestURI)) {
            return new AuthorizationDecision(true);
        }

        boolean checkUrl = allowUri.contains(requestURI);

        if (!checkUrl) {
            return AuthorizationDecision.unAuthorizationDecision();
        }

        if (!LOGIN_REQUEST_URI.equals(requestURI) && authentication == null) {
            return AuthorizationDecision.unAuthorizationDecision();
        }

        if (PRIVATE_REQUEST_URI.equals(requestURI) && authentication.isAuthenticated()) {
            return new AuthorizationDecision(true);
        }

        if (ADMIN_REQUEST_URI.contains(requestURI) && authentication.getAuthorities().contains("ADMIN")) {
            return new AuthorizationDecision(true);
        }

        return AuthorizationDecision.unAuthorizationDecision();
    }
}
