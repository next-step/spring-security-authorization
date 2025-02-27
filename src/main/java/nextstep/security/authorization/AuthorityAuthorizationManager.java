package nextstep.security.authorization;

import nextstep.security.access.RoleHierarchy;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;

public class AuthorityAuthorizationManager<T> implements AuthorizationManager<T> {

    private final String authority;
    private final RoleHierarchy roleHierarchy;

    public AuthorityAuthorizationManager(final String authority, final RoleHierarchy roleHierarchy) {
        this.authority = authority;
        this.roleHierarchy = roleHierarchy;
    }

    @Override
    public AuthorizationDecision check(final Authentication authentication, final T object) {
        if (authentication == null) {
            throw new AuthenticationException();
        }

        return new AuthorizationDecision(hasAuthority(authentication));
    }

    private boolean hasAuthority(final Authentication authentication) {
        return roleHierarchy.getReachableGrantedAuthorities(authentication.getAuthorities())
                .contains(this.authority);
    }
}
