package nextstep.security.authorization;

import java.util.Set;

public interface RoleHierarchy {
    Set<String> getReachableRoleAuthorities(String authority);
}
