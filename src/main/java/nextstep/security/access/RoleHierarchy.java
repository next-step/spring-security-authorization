package nextstep.security.access;

import java.util.Collection;

public interface RoleHierarchy {
    Collection<String> getReachableGrantedAuthorities(final Collection<String> authorities);
}
