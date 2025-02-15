package nextstep.support.mock;

import nextstep.security.role.GrantedAuthority;
import nextstep.security.role.RoleHierarchy;

import java.util.Collection;

public class MockRoleHierarchy implements RoleHierarchy {
    @Override
    public Collection<GrantedAuthority> getReachableRoles(Collection<GrantedAuthority> authorities) {
        return authorities;
    }
}
