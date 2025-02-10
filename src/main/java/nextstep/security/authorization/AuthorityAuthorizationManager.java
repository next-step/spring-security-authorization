package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;

import java.util.Set;

public class AuthorityAuthorizationManager implements AuthorizationManager<HttpServletRequest> {
    private final String authority;

    public AuthorityAuthorizationManager(String authority) {
        this.authority = authority;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest object) {
        Set<String> authorities = authentication.getAuthorities();

        if (!authorities.contains(this.authority)) {
            throw new ForbiddenException();
        }
        return new AuthorizationDecision(true);
    }
}
