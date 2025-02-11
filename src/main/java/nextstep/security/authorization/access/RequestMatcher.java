package nextstep.security.authorization.access;

import jakarta.servlet.http.HttpServletRequest;

public interface RequestMatcher {
    boolean matches(HttpServletRequest request);
}
