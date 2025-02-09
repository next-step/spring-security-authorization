package nextstep.security.authorization.manager;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AccessDeniedException;
import nextstep.security.authorization.AuthorizationDecision;

public class DenyAllAuthorizationManager implements AuthorizationManager<HttpServletRequest> {

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest object) {
        throw new AccessDeniedException();
    }
}
