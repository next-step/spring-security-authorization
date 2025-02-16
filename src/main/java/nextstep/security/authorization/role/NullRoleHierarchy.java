package nextstep.security.authorization.role;

import nextstep.security.authorization.GrantedAuthority;

import java.util.Collection;

public class NullRoleHierarchy implements RoleHierarchy {

    @Override
    public Collection<GrantedAuthority> getReachableGrantedAuthorities(
            Collection<GrantedAuthority> authorities) {

        return authorities;
    }
}
