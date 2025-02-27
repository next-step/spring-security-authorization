package nextstep.security.access;

import java.util.Collection;

public final class NullRoleHierarchy implements RoleHierarchy {

    @Override
    public Collection<String> getReachableGrantedAuthorities(Collection<String> authorities) {
        return authorities;
    }
}
