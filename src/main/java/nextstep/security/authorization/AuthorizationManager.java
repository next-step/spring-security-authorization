package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

@FunctionalInterface
public interface AuthorizationManager<T> {
    AuthorizationDecision check(final Authentication authentication, final T object);
}
