package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.hierarchy.RoleHierarchy;

import java.util.Set;

public class AuthorityAuthorizationManager<T> implements AuthorizationManager<T> {
    private final AuthoritiesAuthorizationManager delegate;

    private final String authority;

    public AuthorityAuthorizationManager(String authority, RoleHierarchy roleHierarchy) {
        this.authority = authority;
        this.delegate = new AuthoritiesAuthorizationManager(roleHierarchy);
    }

    @Override
    public AuthorizationDecision check(final Authentication authentication, final T object) {
        return this.delegate.check(authentication, Set.of(this.authority));
    }
}
