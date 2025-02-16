package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;

public interface RequestMatcher {

    boolean matches(HttpServletRequest request);
}
