package nextstep.security.authorization.role;

public class CircularRoleException extends RuntimeException {
    public CircularRoleException() {
        super("Role 사이의 순환 참조가 존재합니다.");
    }
}
