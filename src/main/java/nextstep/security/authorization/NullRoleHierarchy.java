package nextstep.security.authorization;

import java.util.Collection;

public class NullRoleHierarchy implements RoleHierarchy {
    @Override
    public Collection<String> getReachableGrantedAuthorities(Collection<String> authorities) {
        return authorities;
    }
}
