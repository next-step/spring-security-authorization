package nextstep.security.authorization;

import java.util.Collection;

public interface RoleHierarchy {
    Collection<String> getReachableGrantedAuthorities(
            Collection<String> authorities);
}
