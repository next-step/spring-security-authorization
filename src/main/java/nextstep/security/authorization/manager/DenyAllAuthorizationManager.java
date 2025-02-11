package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;

import static nextstep.security.authorization.manager.AuthorizationDecision.NOT_GRANTED;

public class DenyAllAuthorizationManager<T> implements AuthorizationManager<T> {
    @Override
    public AuthorizationResult authorize(Authentication authentication, T target) {
        return NOT_GRANTED;
    }
}
