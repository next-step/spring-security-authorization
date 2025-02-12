package nextstep.security.authorization.web;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.authorization.AuthorizationDecision;
import nextstep.security.authorization.AuthorizationManager;
import nextstep.security.authorization.ForbiddenException;

public class AuthorityAuthorizationManager implements AuthorizationManager<HttpServletRequest> {

    private final String authority;

    public AuthorityAuthorizationManager(String authority) {
        this.authority = authority;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest object) {
        if (authentication == null) {
            throw new AuthenticationException();
        }

        boolean hasAuthority = authentication.getAuthorities().stream()
                .anyMatch(authority::equals);

        return AuthorizationDecision.of(hasAuthority);

    }
}
