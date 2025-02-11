package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;

public class PermitAllAuthorizationManager<T> implements AuthorizationManager<T> {

    @Override
    public AuthorizationDecision check(Authentication authentication, T object) {
        return AuthorizationDecision.ACCESS_GRANTED;
    }
}
