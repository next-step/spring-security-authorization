package nextstep.security.access;

import java.util.Collection;
import nextstep.security.core.GrantedAuthority;

public final class NullRoleHierarchy implements RoleHierarchy {

    @Override
    public Collection<? extends GrantedAuthority> getReachableGrantedAuthorities(
            Collection<? extends GrantedAuthority> authorities) {
        return authorities;
    }
}
