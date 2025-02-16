package nextstep.security.authorization;

public class AuthorizationDecision {
    private static final AuthorizationDecision SUCCESS = new AuthorizationDecision(true);
    private static final AuthorizationDecision FAIL = new AuthorizationDecision(false);

    private final boolean isSuccess;

    private AuthorizationDecision(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public static AuthorizationDecision success() {
        return SUCCESS;
    }

    public static AuthorizationDecision fail() {
        return FAIL;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
