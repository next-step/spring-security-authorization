package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;

import java.util.Set;

public class AuthorityAuthorizationManager<T> implements AuthorizationManager<T> {

    private final HasAuthorityAuthorizationManager delegate = new HasAuthorityAuthorizationManager();

    private final Set<String> authorities;

    public static <T> AuthorityAuthorizationManager<T> hasAuthority(String authority) {
        return new AuthorityAuthorizationManager<>(authority);
    }

    public static <T> AuthorizationManager<T> permitAll() {
        return (authentication, object) -> AuthorizationDecision.ACCESS_GRANTED;
    }

    public static <T> AuthorizationManager<T> denyAll() {
        return (authentication, httpServletRequest) -> AuthorizationDecision.ACCESS_DENIED;
    }

    public AuthorityAuthorizationManager(String... authorities) {
        this.authorities = Set.of(authorities);
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, T object) {
        return delegate.check(authentication, authorities);
    }
}
