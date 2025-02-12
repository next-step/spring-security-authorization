package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

public class DenyAllAuthorizationManager implements AuthorizationManager {
    @Override
    public AuthorizationDecision check(Authentication authentication, Object object) {
        return AuthorizationDecision.deny();
    }
}
