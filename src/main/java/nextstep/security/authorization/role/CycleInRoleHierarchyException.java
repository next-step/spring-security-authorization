package nextstep.security.authorization.role;

public class CycleInRoleHierarchyException extends RuntimeException {
    public CycleInRoleHierarchyException() {
        super("Exception thrown because of cycle in role hierarchy");
    }
}
