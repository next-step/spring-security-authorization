package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

public class PermitAllAuthorizationManager implements AuthorizationManager {
    @Override
    public AuthorizationDecision check(Authentication authentication, Object object) {
        return new AuthorizationDecision(true);
    }
}
