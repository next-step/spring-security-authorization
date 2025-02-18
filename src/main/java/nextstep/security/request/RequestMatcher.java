package nextstep.security.request;

import jakarta.servlet.http.HttpServletRequest;

public interface RequestMatcher {
    boolean matches(final HttpServletRequest request);
}
