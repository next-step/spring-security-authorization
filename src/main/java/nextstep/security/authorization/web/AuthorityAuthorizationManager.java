package nextstep.security.authorization.web;

import java.util.Collection;
import java.util.Set;
import nextstep.security.access.RoleHierarchy;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.authorization.AuthorizationDecision;
import nextstep.security.authorization.AuthorizationManager;
import nextstep.security.core.GrantedAuthority;

public class AuthorityAuthorizationManager<T> implements AuthorizationManager<T> {
    private final RoleHierarchy roleHierarchy;
    private Collection<String> authorities;

    public AuthorityAuthorizationManager(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
        this.authorities = Set.of();
    }

    public AuthorityAuthorizationManager(RoleHierarchy roleHierarchy, Collection<String> authorities) {
        this.roleHierarchy = roleHierarchy;
        this.authorities = authorities;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, T object) {
        if (authentication == null) {
            throw new AuthenticationException();
        }

        if(object instanceof Collection<?>) {
            this.authorities = (Collection<String>) object;
        }

        boolean hasAuthority = isAuthorized(authentication, authorities);

        return AuthorizationDecision.of(hasAuthority);
    }

    private boolean isAuthorized(Authentication authentication, Collection<String> authorities) {
        for (GrantedAuthority grantedAuthority : getGrantedAuthorities(authentication)) {
            if (authorities.contains(grantedAuthority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    private Collection<GrantedAuthority> getGrantedAuthorities(Authentication authentication) {
        return this.roleHierarchy.getReachableGrantedAuthorities(authentication.getAuthorities());
    }
}
