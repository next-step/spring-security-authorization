package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.hierarchy.RoleHierarchy;

import java.util.Set;

public class AuthorityAuthorizationManager<T> implements AuthorizationManager<T> {
    private final AuthoritiesAuthorizationManager delegate;

    private final Set<String> authorities;

    public AuthorityAuthorizationManager(RoleHierarchy roleHierarchy, String ...authorities) {
        this.delegate = new AuthoritiesAuthorizationManager(roleHierarchy);
        this.authorities = Set.of(authorities);
    }

    @Override
    public AuthorizationDecision check(final Authentication authentication, final T object) {
        return this.delegate.check(authentication, this.authorities);
    }
}
