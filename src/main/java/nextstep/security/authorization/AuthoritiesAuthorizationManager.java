package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.rolehierarchy.RoleHierarchy;

import java.util.Collection;

public class AuthoritiesAuthorizationManager implements AuthorizationManager<Collection<? extends GrantedAuthority>> {

    private final RoleHierarchy roleHierarchy;

    public AuthoritiesAuthorizationManager(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }

    @Override
    public AuthorizationDecision check(final Authentication authentication, final Collection<? extends GrantedAuthority> context) {
        var authorities = roleHierarchy.getReachableGrantedAuthorities(authentication.getAuthorities());
        var matchAuthority = context.stream()
                .anyMatch(authorities::contains);
        if (!matchAuthority) {
            return AuthorizationDecision.fail();
        }
        return AuthorizationDecision.success();
    }
}
