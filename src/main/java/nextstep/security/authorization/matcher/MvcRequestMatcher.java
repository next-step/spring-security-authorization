package nextstep.security.authorization.matcher;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;

public class MvcRequestMatcher implements RequestMatcher{
    private final String pattern;

    private final HttpMethod method;

    public MvcRequestMatcher(final HttpMethod method, final String pattern) {
        this.method = method;
        this.pattern = pattern;
    }

    @Override
    public boolean matches(final HttpServletRequest request) {
        if (method != null && !method.matches(request.getMethod())) {
            return false;
        }

        return request.getRequestURI().matches(pattern);
    }
}
