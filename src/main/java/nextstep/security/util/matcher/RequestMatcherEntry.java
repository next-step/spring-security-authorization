package nextstep.security.util.matcher;

import jakarta.servlet.http.HttpServletRequest;

public class RequestMatcherEntry<T> {

    private final RequestMatcher matcher;
    private final T entry;

    public RequestMatcherEntry(RequestMatcher matcher, T entry) {
        this.matcher = matcher;
        this.entry = entry;
    }

    public boolean matches(HttpServletRequest request) {
        return matcher.matches(request);
    }

    public T getEntry() {
        return entry;
    }
}
