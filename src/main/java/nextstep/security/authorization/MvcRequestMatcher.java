package nextstep.security.authorization;

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
        return request.getMethod().matches(method.name()) && request.getRequestURI().matches(uri);
    }
}
