package nextstep.security.authorization.role;

import nextstep.security.authorization.GrantedAuthority;

import java.util.Collection;

public interface RoleHierarchy {

    Collection<GrantedAuthority> getReachableGrantedAuthorities(
            Collection<GrantedAuthority> authorities);
}
