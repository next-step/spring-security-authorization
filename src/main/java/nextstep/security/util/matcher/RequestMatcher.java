package nextstep.security.util.matcher;

import jakarta.servlet.http.HttpServletRequest;

@FunctionalInterface
public interface RequestMatcher {

    boolean matches(HttpServletRequest request);
}
