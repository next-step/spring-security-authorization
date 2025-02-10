package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;

public class PermitAllAuthorizationManager implements AuthorizationManager<HttpServletRequest> {

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest object) {
        return new AuthorizationDecision(true);
    }
}
