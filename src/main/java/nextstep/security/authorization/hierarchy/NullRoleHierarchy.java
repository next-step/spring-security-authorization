package nextstep.security.authorization.hierarchy;

import java.util.Collection;

public class NullRoleHierarchy implements RoleHierarchy {
    @Override
    public Collection<String> getReachableAuthorities(Collection<String> authorities) {
        return authorities;
    }
}
