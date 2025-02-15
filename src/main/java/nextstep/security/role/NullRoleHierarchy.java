package nextstep.security.role;

import java.util.Collection;

public class NullRoleHierarchy implements RoleHierarchy {
    @Override
    public Collection<GrantedAuthority> getReachableRoles(Collection<GrantedAuthority> authorities) {
        return authorities;
    }
}
