package nextstep.security.authorization.role;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultRoleHierarchy implements RoleHierarchy {
    private final Map<String, Set<String>> hierarchy;

    public DefaultRoleHierarchy(Map<String, Set<String>> hierarchy) {
        this.hierarchy = hierarchy;
    }

    @Override
    public Collection<String> getReachableGrantedAuthorities(Collection<String> authorities) {
        if (authorities == null || authorities.isEmpty()) {
            return List.of();
        }
        final Set<String> reachableRoles = new HashSet<>();
        for (String authority : authorities) {
            traverseHierarchy(authority, reachableRoles);
        }
        return reachableRoles;
    }

    private void traverseHierarchy(String role, Set<String> accumulatedRoles) {
        accumulatedRoles.add(role);
        final Set<String> lowerRoles = hierarchy.getOrDefault(role, Collections.emptySet());
        for (String nextRole : lowerRoles) {
            if (!accumulatedRoles.contains(nextRole)) {
                traverseHierarchy(nextRole, accumulatedRoles);
            }
        }
    }
}
