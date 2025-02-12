package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AccessDeniedException;
import nextstep.security.authorization.AuthorizationDecision;

@FunctionalInterface
public interface AuthorizationManager<T> {

    default void verify(Authentication authentication, T object) {
        AuthorizationDecision decision = check(authentication, object);
        if (decision != null && decision.isDenied()) {
            throw new AccessDeniedException();
        }
    }

    AuthorizationDecision check(Authentication authentication, T object);
}
