package nextstep.security.matcher;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;

public class MvcRequestMatcher implements RequestMatcher {
    private final HttpMethod method;
    private final String uri;

    public MvcRequestMatcher(HttpMethod method, String uri) {
        this.method = method;
        this.uri = uri;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return request.getMethod().equals(this.method.name())
                && request.getRequestURI().equals(this.uri);
    }
}
