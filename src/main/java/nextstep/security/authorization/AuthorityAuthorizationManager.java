package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.rolehierarchy.RoleHierarchy;

import java.util.Set;

public class AuthorityAuthorizationManager<T> implements AuthorizationManager<T> {

    private final AuthoritiesAuthorizationManager authoritiesAuthorizationManager;
    private final Set<GrantedAuthority> authorities;

    public AuthorityAuthorizationManager(final Set<GrantedAuthority> authorities, RoleHierarchy roleHierarchy) {
        this.authorities = authorities;
        this.authoritiesAuthorizationManager = new AuthoritiesAuthorizationManager(roleHierarchy);
    }

    @Override
    public AuthorizationDecision check(final Authentication authentication, final T object) {
        return authoritiesAuthorizationManager.check(authentication, authorities);
    }
}
