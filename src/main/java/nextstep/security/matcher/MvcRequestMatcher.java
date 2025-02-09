package nextstep.security.matcher;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;

import java.util.Arrays;

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
        boolean matchURI = Arrays.stream(requestURIs)
                .anyMatch(uri -> uri.equalsIgnoreCase(request.getRequestURI()));

        return matchMethod && matchURI;
    }
}
