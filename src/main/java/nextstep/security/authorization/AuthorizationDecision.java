package nextstep.security.authorization;

public class AuthorizationDecision {

    private final boolean result;

    private AuthorizationDecision(boolean result) {
        this.result = result;
    }

    public static AuthorizationDecision authorizationDecision() {
        return new AuthorizationDecision(true);
    }

    public static AuthorizationDecision unAuthorizationDecision() {
        return new AuthorizationDecision(false);
    }

    public boolean isAuthorization() {
        return result;
    }
}
