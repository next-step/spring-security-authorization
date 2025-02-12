package nextstep.security.authorization.hierarchy;

import java.util.Collection;

public interface RoleHierarchy {
    Collection<String> getReachableAuthorities(
            Collection<String> authorities);
}
