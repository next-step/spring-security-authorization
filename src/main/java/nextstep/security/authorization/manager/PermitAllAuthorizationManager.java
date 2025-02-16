package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;

import static nextstep.security.authorization.manager.AuthorizationDecision.GRANTED;

public class PermitAllAuthorizationManager<T> implements AuthorizationManager<T> {
    @Override
    public AuthorizationResult authorize(Authentication authentication, T target) {
        return GRANTED;
    }
}
