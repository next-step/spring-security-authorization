package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

public class DenyAllAuthorizationManager implements AuthorizationManager<Object> {

    @Override
    public AuthorizationDecision check(final Authentication authentication, final Object object) {
        return new AuthorizationDecision(false);
    }
}
