package nextstep.security.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;

public class MvcRequestMatcher implements RequestMatcher {

    private final HttpMethod method;
    private final String url;

    public MvcRequestMatcher(HttpMethod method, String url) {
        this.method = method;
        this.url = url;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return request.getMethod().equals(method.name()) && request.getRequestURI().equals(url);
    }
}
