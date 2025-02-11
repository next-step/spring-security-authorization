package nextstep.security.authorization.manager;

public enum AuthorizationDecision {
    GRANTED(true), NOT_GRANTED(false);

    private final boolean granted;

    AuthorizationDecision(boolean granted) {
        this.granted = granted;
    }

    public static AuthorizationDecision of(boolean granted) {
        if (granted) {
            return GRANTED;
        }
        return NOT_GRANTED;
    }

    public boolean isGranted() {
        return this.granted;
    }
}
