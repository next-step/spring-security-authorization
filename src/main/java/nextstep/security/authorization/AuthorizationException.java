package nextstep.security.authorization;

public class AuthorizationException extends RuntimeException {

    public AuthorizationException() {
        super("인가에 실패하였습니다.");
    }

    public AuthorizationException(String message) {
        super(message);
    }

}
