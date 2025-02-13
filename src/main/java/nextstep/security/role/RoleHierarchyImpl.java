package nextstep.security.role;

import java.util.Collection;
import java.util.List;

public class RoleHierarchyImpl implements RoleHierarchy {

    private String role;
    private String implies;

    protected RoleHierarchyImpl() {

    }

    protected RoleHierarchyImpl(String role, String implies) {
        this.role = role;
        this.implies = implies;
    }

    @Override
    public Collection<String> getReachableRoles(Collection<String> authorities) {
        return List.of();
    }

    public static RoleHierarchyImpl with() {
        return new RoleHierarchyImpl();
    }

    public RoleHierarchyImpl role(String role) {
        this.role = role;
        return this;
    }

    public RoleHierarchyImpl implies(String implies) {
        this.implies = implies;
        return this;
    }

    public RoleHierarchy build() {
        return new RoleHierarchyImpl(role, implies);
    }
}
