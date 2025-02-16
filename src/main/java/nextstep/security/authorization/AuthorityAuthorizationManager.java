package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;

public class AuthorityAuthorizationManager implements AuthorizationManager<HttpServletRequest> {
    private final String allowRole;

    public AuthorityAuthorizationManager(String allowRole) {
        this.allowRole = allowRole;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest request) {
        if (authentication == null) {
            return AuthorizationDecision.unAuthorizationDecision();
        }

        if (authentication.isAuthenticated() && authentication.getAuthorities().contains(allowRole)) {
            return AuthorizationDecision.authorizationDecision();
        }

        return AuthorizationDecision.unAuthorizationDecision();
    }
}
