package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.Map;

public class RequestAuthorizationContext {

    private final HttpServletRequest request;
    private final Map<String, String> variables;

    public RequestAuthorizationContext(HttpServletRequest request) {
        this(request, Collections.emptyMap());
    }

    public RequestAuthorizationContext(HttpServletRequest request, Map<String, String> variables) {
        this.request = request;
        this.variables = variables;
    }

    public HttpServletRequest getRequest() {
        return this.request;
    }

    public Map<String, String> getVariables() {
        return this.variables;
    }
}
