package nextstep.security.request;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;

public class MvcRequestMatcher implements RequestMatcher {

    private final HttpMethod method;
    private final String uri;

    public MvcRequestMatcher(final HttpMethod method, final String uri) {
        this.method = method;
        this.uri = uri;
    }

    @Override
    public boolean matches(final HttpServletRequest request) {
        return request.getMethod().equals(method.name()) && request.getRequestURI().equals(uri);
    }
}
