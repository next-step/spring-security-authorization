package nextstep.security.authorization;

public class AuthorizationDecision {
    private final boolean isGranted;

    public AuthorizationDecision(boolean granted) {
        this.granted = granted;
    }

    public boolean isGranted() {
        return granted;
    }

    public static AuthorizationDecision of(boolean granted) {
        return granted ? ALLOW : DENY;
    }
}
