package nextstep.security.authorization;

public class AuthorizationDecision {
    public static final AuthorizationDecision ACCESS_DENIED = new AuthorizationDecision(false);
    public static final AuthorizationDecision ACCESS_GRANTED = new AuthorizationDecision(true);

    private final boolean isGranted;

    public AuthorizationDecision(boolean isGranted) {
        this.isGranted = isGranted;
    }

    public boolean isGranted() {
        return isGranted;
    }

    public boolean isDenied() {
        return !isGranted;
    }
}
