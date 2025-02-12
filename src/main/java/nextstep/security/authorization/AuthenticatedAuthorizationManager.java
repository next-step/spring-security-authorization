package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;

import java.util.Objects;

public class AuthenticatedAuthorizationManager implements AuthorizationManager<HttpServletRequest> {
    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest object) {
        if (Objects.isNull(authentication) || !authentication.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }
        return new AuthorizationDecision(true);
    }
}
