package nextstep.security.authorization.matcher;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authorization.manager.AuthorizationManager;
import org.springframework.http.HttpMethod;

public record RequestMatcherEntry<T>(
        RequestMatcher requestMatcher,
        T entry
) {
    public static RequestMatcherEntry<AuthorizationManager<HttpServletRequest>> createMvcMatcher(
            HttpMethod method, String pattern,
            AuthorizationManager<HttpServletRequest> authorizationManager
    ) {
        return new RequestMatcherEntry<>(
                new MvcRequestMatcher(method, pattern),
                authorizationManager
        );
    }

    public static RequestMatcherEntry<AuthorizationManager<HttpServletRequest>> createDefaultMatcher(
            AuthorizationManager<HttpServletRequest> authorizationManager
    ) {
        return new RequestMatcherEntry<>(
                AnyRequestMatcher.getInstance(),
                authorizationManager
        );
    }
}
