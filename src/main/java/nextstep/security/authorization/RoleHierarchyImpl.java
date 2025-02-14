package nextstep.security.authorization;

import java.util.*;

public class RoleHierarchyImpl implements RoleHierarchy {
    private final Map<String, Set<String>> hierarchyPath;

    private RoleHierarchyImpl(Map<String, Set<String>> hierarchyPath) {
        this.hierarchyPath = hierarchyPath;
    }

    @Override
    public Collection<String> getReachableGrantedAuthorities(Collection<String> authorities) {
        Set<String> grantedAuthorities = new HashSet<>();

        for (String authority : authorities) {
            if (hierarchyPath.get(authority) != null) {
                grantedAuthorities.addAll(hierarchyPath.get(authority));
            }
        }

        return grantedAuthorities;
    }

    public static RoleBuilder with() {
        return new RoleBuilder();
    }

    public static class RoleBuilder {
        private static final Map<String, Set<String>> hierarchy = new HashMap<>();
        private String role;

        public RoleBuilder role(String role) {
            this.role = role;
            hierarchy.put(role, new HashSet<>(Set.of(role)));
            return this;
        }

        public ImpliesBuilder implies(String... implies) {
            hierarchy.get(role).addAll(Set.of(implies));

            for (int i = 0; i < implies.length; i++) {
                Set<String> paths = hierarchy.getOrDefault(implies[i], new HashSet<>());
                paths.addAll(List.of(implies).subList(i, implies.length));
                hierarchy.put(implies[i], paths);
            }

            return new ImpliesBuilder();
        }

        public static class ImpliesBuilder {
            public RoleHierarchyImpl build() {
                return new RoleHierarchyImpl(hierarchy);
            }
        }
    }
}
