package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;

public class MvcRequestMatcher implements RequestMatcher {

    private final HttpMethod httpMethod;

    private final String uri;

    public MvcRequestMatcher(HttpMethod httpMethod, String uri) {
        this.httpMethod = httpMethod;
        this.uri = uri;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        final HttpMethod requestMethod = HttpMethod.valueOf(request.getMethod());
        final String requestUri = request.getRequestURI();

        return equals(requestMethod, requestUri);
    }

    private boolean equals(final HttpMethod requestMethod, final String requestUri) {
        return this.httpMethod.equals(requestMethod) && this.uri.equals(requestUri);
    }
}
