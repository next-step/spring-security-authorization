package nextstep.security.authorization.manager;

public enum AuthorizationDecision implements AuthorizationResult {
    GRANTED(true),
    NOT_GRANTED(false);

    private final boolean granted;

    AuthorizationDecision(final boolean granted) {
        this.granted = granted;
    }

    public boolean isGranted() {
        return this.granted;
    }

    public static AuthorizationResult from(boolean granted) {
        return granted ? GRANTED : NOT_GRANTED;
    }
}
