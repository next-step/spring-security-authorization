package nextstep.security.authorization.manager;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;
import nextstep.security.authorization.ForbiddenException;

import java.util.Set;

public class AuthorityAuthorizationManager implements AuthorizationManager<HttpServletRequest> {

    private final Set<String> authorities;

    public AuthorityAuthorizationManager(Set<String> authorities) {
        this.authorities = authorities;
    }


    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest request) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return AuthorizationDecision.denied();
        }

        boolean hasNoRole = authentication.getAuthorities()
                .stream()
                .noneMatch(authorities::contains);

        if (hasNoRole) {
            throw new ForbiddenException();
        }

        return AuthorizationDecision.granted();
    }
}
