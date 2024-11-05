package nextstep.security.authorization;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException() {
        super("접근에 실패하였습니다.");
    }

    public AccessDeniedException(String message) {
        super(message);
    }

}
