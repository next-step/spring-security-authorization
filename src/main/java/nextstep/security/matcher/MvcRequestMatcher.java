package nextstep.security.matcher;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

public class MvcRequestMatcher implements RequestMatcher {
    private final HttpMethod method;
    private final PathPattern pattern;

    public MvcRequestMatcher(HttpMethod method, String pattern) {
        this.method = method;
        this.pattern = new PathPatternParser().parse(pattern);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        if (!method.matches(request.getMethod())) {
            return false;
        }

        return pattern.matches(PathContainer.parsePath(request.getRequestURI()));
    }
}
