package nextstep.security.role;

import java.util.Collection;

public interface RoleHierarchy {
    Collection<GrantedAuthority> getReachableRoles(Collection<GrantedAuthority> authorities);
}
