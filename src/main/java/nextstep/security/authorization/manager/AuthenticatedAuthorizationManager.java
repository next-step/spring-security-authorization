package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;

import static nextstep.security.authorization.manager.AuthorizationDecision.GRANTED;
import static nextstep.security.authorization.manager.AuthorizationDecision.NOT_GRANTED;

public class AuthenticatedAuthorizationManager<T> implements AuthorizationManager<T> {
    @Override
    public AuthorizationResult authorize(Authentication authentication, T target) {
        if(authentication != null &&
                authentication.isAuthenticated()
            ) {
            return GRANTED;
        }

        return NOT_GRANTED;
    }
}
