package nextstep.security.authorization;

public class AuthorizationDecision {
    private final boolean granted;

    private AuthorizationDecision(boolean granted) {
        this.granted = granted;
    }

    public static AuthorizationDecision deny() {
        return new AuthorizationDecision(false);
    }

    public static AuthorizationDecision granted() {
        return new AuthorizationDecision(true);
    }

    public boolean isDeny() {
        return !granted;
    }
}
