package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.rolehierarchy.NullRoleHierarchy;
import nextstep.security.rolehierarchy.RoleHierarchy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RoleHierarchyAuthoritiesAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext<Collection<GrantedAuthority>>> {

    private final RoleHierarchy roleHierarchy;
    private final AuthorizationManager<RequestAuthorizationContext<Collection<GrantedAuthority>>> delegate;

    public RoleHierarchyAuthoritiesAuthorizationManager() {
        roleHierarchy = new NullRoleHierarchy();
        this.delegate = new AuthoritiesAuthorizationManager();
    }

    public RoleHierarchyAuthoritiesAuthorizationManager(final RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
        this.delegate = new AuthoritiesAuthorizationManager();
    }

    @Override
    public AuthorizationDecision check(final Authentication authentication, final RequestAuthorizationContext<Collection<GrantedAuthority>> context) {
        return delegate.check(authentication, context);
    }

}
