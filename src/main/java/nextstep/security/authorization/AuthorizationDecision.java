package nextstep.security.authorization;

public class AuthorizationDecision {
    private final boolean isGranted;

    protected AuthorizationDecision(final boolean isGranted) {
        this.isGranted = isGranted;
    }

    public static AuthorizationDecision granted() {
        return new AuthorizationDecision(true);
    }

    public static AuthorizationDecision denied() {
        return new AuthorizationDecision(false);
    }

    public boolean isDenied() {
        return !isGranted;
    }
}
