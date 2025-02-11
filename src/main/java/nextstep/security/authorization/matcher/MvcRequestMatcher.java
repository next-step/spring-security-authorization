package nextstep.security.authorization.matcher;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;

import java.util.regex.Pattern;

public class MvcRequestMatcher implements RequestMatcher {
    private final HttpMethod method;
    private final Pattern pattern;

    public MvcRequestMatcher(HttpMethod method, String pattern) {
        this.method = method;
        this.pattern = compile(pattern);
    }

    private Pattern compile(String regex) {
        if (regex == null || regex.isBlank()) {
            return null;
        }
        return Pattern.compile(regex);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return matchMethod(request) && matchPattern(request);
    }

    private boolean matchMethod(HttpServletRequest request) {
        return method == null || method.name().equals(request.getMethod());
    }

    private boolean matchPattern(HttpServletRequest request) {
        return pattern == null || pattern.matcher(request.getRequestURI()).matches();
    }
}
