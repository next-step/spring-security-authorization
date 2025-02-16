package nextstep.security.authorization.role;

import java.util.Collection;

public interface RoleHierarchy {
    Collection<String> getReachableGrantedAuthorities(Collection<String> authorities);
}
