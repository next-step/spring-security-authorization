package nextstep.security.authorization.matcher;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;

public class MvcRequestMatcher implements RequestMatcher {

    public final HttpMethod method;

    public final String uri;

    public MvcRequestMatcher(HttpMethod method, String uri) {
        this.method = method;
        this.uri = uri;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        HttpMethod resolve = HttpMethod.resolve(request.getMethod());

        if (resolve != method) {
            return false;
        }

        String requestUri = request.getRequestURI();

        if (!uri.equals(requestUri)) {
            return false;
        }

        return true;
    }
}
