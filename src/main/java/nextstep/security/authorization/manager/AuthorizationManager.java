package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;

@FunctionalInterface
public interface AuthorizationManager<T> {
    AuthorizationDecision check(Authentication authentication, T object);
}
