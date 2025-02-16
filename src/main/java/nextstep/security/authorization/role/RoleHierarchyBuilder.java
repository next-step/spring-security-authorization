package nextstep.security.authorization.role;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class RoleHierarchyBuilder {
    private final Map<String, Set<String>> hierarchy = new LinkedHashMap<>();

    public ImpliedRoles role(String role) {
        return new ImpliedRoles(role);
    }

    public RoleHierarchyImpl build() {
        return new RoleHierarchyImpl(hierarchy);
    }

    private RoleHierarchyBuilder implyRoles(String role, String... impliedRoles) {
        hierarchy.put(role, Set.of(impliedRoles));
        return this;
    }

    public final class ImpliedRoles {
        private final String role;

        private ImpliedRoles(String role) {
            this.role = role;
        }

        public RoleHierarchyBuilder implies(String... impliedRoles) {
            return implyRoles(role, impliedRoles);
        }
    }
}
