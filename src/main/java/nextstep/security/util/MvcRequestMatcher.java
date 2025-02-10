package nextstep.security.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;

public class MvcRequestMatcher implements RequestMatcher {

    private final HttpMethod method;
    private final String pattern;

    public MvcRequestMatcher(HttpMethod method, String pattern) {
        this.method = method;
        this.pattern = pattern;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return this.method.equals(HttpMethod.valueOf(request.getMethod())) && request.getRequestURI().equals(pattern);
    }
}
