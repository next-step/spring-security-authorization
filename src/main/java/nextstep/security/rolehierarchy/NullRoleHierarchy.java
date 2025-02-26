package nextstep.security.rolehierarchy;

import nextstep.security.authorization.GrantedAuthority;

import java.util.Collection;

public class NullRoleHierarchy implements RoleHierarchy {

    @Override
    public Collection<? extends GrantedAuthority> getReachableGrantedAuthorities(Collection<? extends GrantedAuthority> grantedAuthorities) {
        return grantedAuthorities;
    }

}
