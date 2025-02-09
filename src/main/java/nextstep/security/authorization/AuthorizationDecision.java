package nextstep.security.authorization;

public class AuthorizationDecision {
    private final boolean isAuthorized;

    public AuthorizationDecision(boolean result) {
        this.isAuthorized = result;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }
}
