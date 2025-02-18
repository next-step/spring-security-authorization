package nextstep.security.authorization.role;

import java.util.Collection;

public interface RoleHierarchy {
    Collection<GrantedAuthority> getReachableGrantedAuthorities(
            Collection<GrantedAuthority> authorities
    );
}
