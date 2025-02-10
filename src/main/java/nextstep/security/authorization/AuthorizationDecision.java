package nextstep.security.authorization;

public class AuthorizationDecision {
    final boolean result;

    public AuthorizationDecision(boolean result) {
        this.result = result;
    }

    public static AuthorizationDecision unAuthorizationDecision() {
        return new AuthorizationDecision(false);
    }
}
