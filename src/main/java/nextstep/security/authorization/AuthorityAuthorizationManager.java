package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;

import java.util.Objects;
import java.util.Set;

public class AuthorityAuthorizationManager implements AuthorizationManager<HttpServletRequest> {
    private final String authority;

    public AuthorityAuthorizationManager(String authority) {
        this.authority = authority;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest object) {
        if (Objects.isNull(authentication)) {
            return new AuthorizationDecision(false);
        }
        Set<String> authorities = authentication.getAuthorities();

        if (!authorities.contains(this.authority)) {
            return new AuthorizationDecision(false);
        }
        return new AuthorizationDecision(true);
    }
}
