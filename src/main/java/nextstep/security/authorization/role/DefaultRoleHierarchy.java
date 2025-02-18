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
        validateCircularRole();
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
        final Set<String> lowerRoles = lowerRoles(role);
        for (String nextRole : lowerRoles) {
            if (!accumulatedRoles.contains(nextRole)) {
                traverseHierarchy(nextRole, accumulatedRoles);
            }
        }
    }

    private void validateCircularRole() {
        final Set<String> accumulatedRoles = new HashSet<>();
        for (String rootRole : hierarchy.keySet()) {
            validateCircularRole(rootRole, accumulatedRoles);
        }
    }

    private void validateCircularRole(String role, Set<String> accumulatedRoles) {
        accumulatedRoles.add(role);
        final Set<String> lowerRoles = lowerRoles(role);
        for (String nextRole : lowerRoles) {
            if (accumulatedRoles.contains(nextRole)) {
                throw new CircularRoleException();
            }
        }
    }

    private Set<String> lowerRoles(String role) {
        return hierarchy.getOrDefault(role, Collections.emptySet());
    }
}
