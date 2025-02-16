package nextstep.security.rolehierarchy;

import nextstep.security.authorization.GrantedAuthority;

import java.util.Collection;

public interface RoleHierarchy {
    Collection<GrantedAuthority> getReachableGrantedAuthorities(Collection<GrantedAuthority> grantedAuthorities);

}
