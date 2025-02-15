package nextstep.security.access;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import nextstep.security.core.GrantedAuthority;
import nextstep.security.core.SimpleGrantedAuthority;

public class RoleHierarchyImpl implements RoleHierarchy {

    private Map<String, Set<GrantedAuthority>> rolesReachableInOneOrMoreStepsMap = null;

    private RoleHierarchyImpl(Map<String, Set<GrantedAuthority>> hierarchy) {
        this.rolesReachableInOneOrMoreStepsMap = buildRolesReachableInOneOrMoreStepsMap(hierarchy);
    }

    public static Builder with() {
        return new Builder();
    }

    @Override
    public Collection<GrantedAuthority> getReachableGrantedAuthorities(
            Collection<GrantedAuthority> authorities) {
        if (authorities == null || authorities.isEmpty()) {
            return Collections.emptyList();
        }

        Set<GrantedAuthority> reachableRoles = new HashSet<>();
        Set<String> processedNames = new HashSet<>();

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority() == null) {
                reachableRoles.add(authority);
                continue;
            }

            if (!processedNames.add(authority.getAuthority())) {
                continue;
            }

            reachableRoles.add(authority);

            Set<GrantedAuthority> lowerRoles = this.rolesReachableInOneOrMoreStepsMap.get(authority.getAuthority());
            if (lowerRoles == null) {
                continue;
            }
            for (GrantedAuthority role : lowerRoles) {
                if (processedNames.add(role.getAuthority())) {
                    reachableRoles.add(role);
                }
            }
        }

        return new ArrayList<>(reachableRoles);
    }

    private static Map<String, Set<GrantedAuthority>> buildRolesReachableInOneOrMoreStepsMap(
            Map<String, Set<GrantedAuthority>> hierarchy) {
        Map<String, Set<GrantedAuthority>> rolesReachableInOneOrMoreStepsMap = new HashMap<>();
        for (String roleName : hierarchy.keySet()) {
            Set<GrantedAuthority> rolesToVisitSet = new HashSet<>(hierarchy.get(roleName));
            Set<GrantedAuthority> visitedRolesSet = new HashSet<>();
            while (!rolesToVisitSet.isEmpty()) {
                GrantedAuthority lowerRole = rolesToVisitSet.iterator().next();
                rolesToVisitSet.remove(lowerRole);
                if (!visitedRolesSet.add(lowerRole) || !hierarchy.containsKey(lowerRole.getAuthority())) {
                    continue;
                } else if (roleName.equals(lowerRole.getAuthority())) {
                    throw new RuntimeException();
                }
                rolesToVisitSet.addAll(hierarchy.get(lowerRole.getAuthority()));
            }
            rolesReachableInOneOrMoreStepsMap.put(roleName, visitedRolesSet);
        }
        return rolesReachableInOneOrMoreStepsMap;
    }

    public static final class Builder {

        private final Map<String, Set<GrantedAuthority>> hierarchy;

        private Builder() {
            this.hierarchy = new LinkedHashMap<>();
        }

        public ImpliedRoles role(String role) {
            return new ImpliedRoles(role);
        }

        public RoleHierarchyImpl build() {
            return new RoleHierarchyImpl(this.hierarchy);
        }

        private Builder addHierarchy(String role, String... impliedRoles) {
            Set<GrantedAuthority> withPrefix = this.hierarchy.computeIfAbsent(role
                    , (r) -> new HashSet<>());
            for (String impliedRole : impliedRoles) {
                withPrefix.add(new SimpleGrantedAuthority(impliedRole));
            }
            return this;
        }

        public final class ImpliedRoles {

            private final String role;

            private ImpliedRoles(String role) {
                this.role = role;
            }

            public Builder implies(String... impliedRoles) {
                return Builder.this.addHierarchy(this.role, impliedRoles);
            }
        }
    }
}
