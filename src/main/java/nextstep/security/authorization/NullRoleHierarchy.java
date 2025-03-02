package nextstep.security.authorization;

import java.util.Set;

public class NullRoleHierarchy implements RoleHierarchy {
    @Override
    public Set<String> getReachableRoleAuthorities(String authority) {
        return Set.of(authority);
    }
}
