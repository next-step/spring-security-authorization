package nextstep.security.authorization;

import org.springframework.util.CollectionUtils;

import java.util.*;

public class RoleHierarchyImpl implements RoleHierarchy {

    private final Map<String, Set<String>> rolesReachableInOneOrMoreStepsMap;

    public RoleHierarchyImpl(Map<String, Set<String>> rolesReachableInOneOrMoreStepsMap) {
        this.rolesReachableInOneOrMoreStepsMap = rolesReachableInOneOrMoreStepsMap;
    }

    public static Builder with() {
        return new Builder();
    }

    @Override
    public Collection<String> getReachableGrantedAuthorities(Collection<String> authorities) {

        Set<String> reachableRoles = new HashSet<>();

        if (CollectionUtils.isEmpty(authorities)) {
            return Collections.emptyList();
        }

        for (String authority : authorities) {
            addReachableRoles(authority, reachableRoles);
        }

        return reachableRoles;
    }

    private void addReachableRoles(String authority, Set<String> reachableRoles) {
        reachableRoles.add(authority);
        Set<String> lowerRoles = rolesReachableInOneOrMoreStepsMap.computeIfAbsent(authority, k -> new HashSet<>());
        reachableRoles.addAll(lowerRoles);
    }

    public static class Builder {
        private final Map<String, Set<String>> hierarchy;

        private Builder() {
            this.hierarchy = new HashMap<>();
        }

        public RoleHierarchyImpl build() {
            return new RoleHierarchyImpl(hierarchy);
        }

        public ImpliedRoles role(String role) {
            return new ImpliedRoles(role);
        }

        private Builder addHierarchy(String role, String... impliedRoles) {
            Set<String> impliedRoleSet = hierarchy.computeIfAbsent(role, k -> new HashSet<>());
            impliedRoleSet.addAll(Arrays.asList(impliedRoles));
            return this;
        }

        public final class ImpliedRoles {
            private final String role;

            public ImpliedRoles(String role) {
                this.role = role;
            }

            public Builder implies(String... impliedRoles) {
                return Builder.this.addHierarchy(this.role, impliedRoles);
            }
        }
    }
}

