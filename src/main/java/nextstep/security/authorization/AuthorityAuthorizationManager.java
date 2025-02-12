package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;

public class AuthorityAuthorizationManager implements AuthorizationManager<HttpServletRequest> {
    private final String allowRole;

    public AuthorityAuthorizationManager(String allowRole) {
        this.allowRole = allowRole;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest request) {
        if (authentication == null) {
            throw new AuthenticationException();
        }

        if (authentication.isAuthenticated() && authentication.getAuthorities().contains(allowRole)) {
            return new AuthorizationDecision(true);
        }

        return AuthorizationDecision.unAuthorizationDecision();
    }
}
