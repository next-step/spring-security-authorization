package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.web.AuthorizationResult;

@FunctionalInterface
public interface AuthorizationManager<T> {
    @Deprecated
    AuthorizationDecision check(Authentication authentication, T object);

    default AuthorizationResult authorize(Authentication authentication, T object) {
        return check(authentication, object);
    }
}
