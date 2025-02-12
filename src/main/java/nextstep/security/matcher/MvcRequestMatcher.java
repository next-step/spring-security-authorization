package nextstep.security.matcher;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;

public class MvcRequestMatcher implements RequestMatcher {

    private final HttpMethod httpMethod;
    private final String[] requestURIs;

    public MvcRequestMatcher(HttpMethod httpMethod, String... requestURIs) {
        this.httpMethod = httpMethod;
        this.requestURIs = requestURIs;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        boolean matchMethod = httpMethod.name().equalsIgnoreCase(request.getMethod());

        boolean matchURI = false;
        final String requestURI = request.getRequestURI();
        for (String uri : requestURIs) {
            if (uri.equalsIgnoreCase(requestURI)) {
                matchURI = true;
                break;
            }
        }

        return matchMethod && matchURI;
    }
}
