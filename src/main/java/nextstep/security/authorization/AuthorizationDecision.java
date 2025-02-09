package nextstep.security.authorization;

public class AuthorizationDecision {
    private final boolean isGranted;

    public AuthorizationDecision(boolean isGranted) {
        this.isGranted = isGranted;
    }

    public boolean isGranted() {
        return isGranted;
    }
}
