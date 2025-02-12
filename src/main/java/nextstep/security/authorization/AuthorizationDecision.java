package nextstep.security.authorization;

public class AuthorizationDecision {

    private static final AuthorizationDecision GRANTED = new AuthorizationDecision(true);
    private static final AuthorizationDecision DENIED = new AuthorizationDecision(false);

    private final boolean isGranted;

    public static AuthorizationDecision granted() {
        return GRANTED;
    }

    public static AuthorizationDecision denied() {
        return DENIED;
    }

    protected AuthorizationDecision(final boolean isGranted) {
        this.isGranted = isGranted;
    }

    public boolean isDenied() {
        return !isGranted;
    }
}
