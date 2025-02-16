package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;

public interface RequestAuthorizationContext<T> {
    HttpServletRequest getRequest();

    T getOthers();
}
