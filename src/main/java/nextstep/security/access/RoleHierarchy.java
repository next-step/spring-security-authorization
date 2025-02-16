package nextstep.security.access;

import java.util.Collection;
import nextstep.security.core.GrantedAuthority;

public interface RoleHierarchy {
    Collection<GrantedAuthority> getReachableGrantedAuthorities(
            Collection<GrantedAuthority> authorities);
}
