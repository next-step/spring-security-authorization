package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;

@FunctionalInterface
public interface AuthorizationManager<T> {
    AuthorizationDecision check(Authentication authentication, T target);
}
