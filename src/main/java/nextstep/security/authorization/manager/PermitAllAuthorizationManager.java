package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;

import static nextstep.security.authorization.manager.AuthorizationDecision.GRANTED;

public class PermitAllAuthorizationManager<T> implements AuthorizationManager<T> {
    @Override
    public AuthorizationDecision check(Authentication authentication, T target) {
        return GRANTED;
    }
}
