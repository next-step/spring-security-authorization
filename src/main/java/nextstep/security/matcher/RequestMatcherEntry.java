package nextstep.security.matcher;

import jakarta.servlet.http.HttpServletRequest;

public class RequestMatcherEntry<T> {
    private final RequestMatcher requestMatcher;
    private final T entry;

    public RequestMatcherEntry(RequestMatcher requestMatcher, T entry) {
        this.requestMatcher = requestMatcher;
        this.entry = entry;
    }

    public boolean matches(HttpServletRequest request) {
        return requestMatcher.matches(request);
    }

    public T getEntry() {
        return this.entry;
    }
}