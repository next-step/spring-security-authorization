package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;

public class RequestMatcherEntry<T extends AuthorizationManager> {

    private final RequestMatcher requestMatcher;
    private final T entry;

    public RequestMatcherEntry(RequestMatcher requestMatcher, T entry) {
        this.requestMatcher = requestMatcher;
        this.entry = entry;
    }

    public boolean isMatches(HttpServletRequest request) {
        return requestMatcher.matches(request);
    }

    public AuthorizationDecision check(Authentication authentication, HttpServletRequest request) {
        return entry.check(authentication, request);
    }
}
