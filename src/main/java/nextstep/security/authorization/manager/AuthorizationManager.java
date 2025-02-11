package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;

@FunctionalInterface
public interface AuthorizationManager<T> {
    AuthorizationResult authorize(Authentication authentication, T target);
}
