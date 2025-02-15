package nextstep.security.util.matcher;

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
        return method.matches(request.getMethod()) && pattern.equals(request.getRequestURI());
    }
}
