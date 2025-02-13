package nextstep.security.authorization.hierarchy;

import org.springframework.util.Assert;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class RoleHierarchyImpl implements RoleHierarchy {
    private final Map<String, Set<String>> rolesReachableInOneOrMoreStepsMap;

    private RoleHierarchyImpl(final Map<String, Set<String>> rolesReachableInOneOrMoreStepsMap) {
        this.rolesReachableInOneOrMoreStepsMap = rolesReachableInOneOrMoreStepsMap;
    }

    public static Builder with() {
        return new Builder();
    }

    @Override
    public Collection<String> getReachableAuthorities(Collection<String> authorities) {
        if (authorities == null || authorities.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> reachableRoles = new HashSet<>();
        Set<String> processedRoles = new HashSet<>();
        Queue<String> queue = new ArrayDeque<>(authorities);

        while (!queue.isEmpty()) {
            String role = queue.poll();
            if (!processedRoles.add(role)) {
                continue;
            }

            reachableRoles.add(role);

            Set<String> lowerRoles = this.rolesReachableInOneOrMoreStepsMap.get(role);
            if (lowerRoles != null) {
                queue.addAll(lowerRoles);
            }
        }

        return new ArrayList<>(reachableRoles);
    }

    public static class Builder {
        private final Map<String, Set<String>> hierarchy;

        private Builder() {
            this.hierarchy = new LinkedHashMap<>();
        }

        public ImpliedRoles role(String role) {
            Assert.hasText(role, "role must not be empty");
            return new ImpliedRoles(role);
        }

        public RoleHierarchyImpl build() {
            detectCycle();
            return new RoleHierarchyImpl(this.hierarchy);
        }

        private void detectCycle() {
            Set<String> visited = new HashSet<>();
            Set<String> stack = new HashSet<>();

            for (String role : hierarchy.keySet()) {
                if (hasCycle(role, visited, stack)) {
                    throw new CycleInRoleHierarchyException();
                }
            }
        }

        private boolean hasCycle(String role, Set<String> visited, Set<String> stack) {
            if (stack.contains(role)) {
                return true;
            }
            if (visited.contains(role)) {
                return false;
            }

            visited.add(role);
            stack.add(role);

            Set<String> children = hierarchy.get(role);
            if (children != null) {
                for (String child : children) {
                    if (hasCycle(child, visited, stack)) {
                        return true;
                    }
                }
            }

            stack.remove(role);
            return false;
        }

        private Builder addHierarchy(String role, String... impliedRoles) {
            Set<String> childRoles = Set.of(impliedRoles);

            this.hierarchy.put(role, childRoles);
            return this;
        }

        public final class ImpliedRoles {

            private final String role;

            private ImpliedRoles(String role) {
                this.role = role;
            }

            public Builder implies(String... impliedRoles) {
                Assert.notEmpty(impliedRoles, "at least one implied role must be provided");
                Assert.noNullElements(impliedRoles, "implied role name(s) cannot be empty");
                return Builder.this.addHierarchy(this.role, impliedRoles);
            }
        }
    }
}
