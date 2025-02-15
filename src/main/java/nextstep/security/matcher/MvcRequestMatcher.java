package nextstep.security.matcher;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

public class MvcRequestMatcher implements RequestMatcher {

    private final HttpMethod httpMethod;
    private final String requestURI;

    public MvcRequestMatcher(HttpMethod httpMethod, String requestURI) {
        if (!StringUtils.hasText(requestURI)) {
            throw new IllegalArgumentException("requestURI must not be null or empty");
        }
        if (httpMethod == null) {
            throw new IllegalArgumentException("httpMethod must not be null");
        }

        this.httpMethod = httpMethod;
        this.requestURI = requestURI;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        boolean matchMethod = httpMethod.name().equalsIgnoreCase(request.getMethod());
        boolean matchURI = requestURI.equalsIgnoreCase(request.getRequestURI());

        return matchMethod && matchURI;
    }
}
