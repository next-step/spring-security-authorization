package nextstep.security.authorization.manager;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;

import java.util.Set;

public class AuthorityAuthorizationManager<T> implements AuthorizationManager<T> {

    private final AuthoritiesAuthorizationManager delegate = new AuthoritiesAuthorizationManager();

    private final Set<String> authorities;

    public static <T> AuthorityAuthorizationManager<T> hasAuthority(String authority) {
        return new AuthorityAuthorizationManager<>(authority);
    }

    public static <T> AuthorizationManager<T> permitAll() {
        return (authentication, object) -> AuthorizationDecision.ACCESS_GRANTED;
    }

    public AuthorityAuthorizationManager(String... authorities) {
        this.authorities = Set.of(authorities);
    }

    public static AuthorizationManager<HttpServletRequest> denyAll() {
        return (authentication, httpServletRequest) -> AuthorizationDecision.ACCESS_DENIED;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, T object) {
        return delegate.check(authentication, authorities);
    }
}
