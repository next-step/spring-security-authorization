package nextstep.security.authorization.manager;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;

public class RequestAuthorizationManager implements AuthorizationManager<HttpServletRequest> {
    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest target) {
        return target.getRequestURI().equals("/members")
                && !authentication.getAuthorities().contains("ADMIN")
                ? AuthorizationDecision.NOT_GRANTED
                : AuthorizationDecision.GRANTED;
    }
}
