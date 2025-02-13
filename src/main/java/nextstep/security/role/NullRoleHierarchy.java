package nextstep.security.role;

import java.util.Collection;
import java.util.Collections;

public class NullRoleHierarchy implements RoleHierarchy {
    @Override
    public Collection<GrantedAuthority> getReachableRoles(Collection<GrantedAuthority> authorities) {
        return Collections.emptyList();
    }
}
