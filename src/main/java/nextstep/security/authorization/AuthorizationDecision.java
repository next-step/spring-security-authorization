package nextstep.security.authorization;

public record AuthorizationDecision(
        boolean granted
) {

    public boolean denied() {
        return !granted;
    }

    public static AuthorizationDecision grantedOf() {
        return new AuthorizationDecision(true);
    }

    public static AuthorizationDecision deniedOf() {
        return new AuthorizationDecision(false);
    }
}
