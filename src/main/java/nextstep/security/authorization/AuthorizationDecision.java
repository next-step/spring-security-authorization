package nextstep.security.authorization;

import nextstep.security.authorization.web.AuthorizationResult;

public class AuthorizationDecision implements AuthorizationResult {
    public static final AuthorizationDecision ALLOW = new AuthorizationDecision(true);
    public static final AuthorizationDecision DENY = new AuthorizationDecision(false);

    private final boolean granted;

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
