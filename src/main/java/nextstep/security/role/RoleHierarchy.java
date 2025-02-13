package nextstep.security.role;

import java.util.Collection;

public interface RoleHierarchy {
    Collection<String> getReachableRoles(Collection<String> authorities);
}
