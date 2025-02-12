package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

@FunctionalInterface
public interface AuthorizationManager<T> {

    AuthorizationDecision check(Authentication authentication, T object);

    default void verify(Authentication authentication, T object) throws ForbiddenException{
        if (check(authentication, object).isDeny()) {
            throw new ForbiddenException();
        }
    }
}
