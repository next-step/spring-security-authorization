package nextstep.security.authorization.access;

public class RequestMatcherEntry<T> {

    private final RequestMatcher requestMatcher;
    private final T entry;

    public RequestMatcherEntry(RequestMatcher requestMatcher, T entry) {
        this.requestMatcher = requestMatcher;
        this.entry = entry;
    }

    public RequestMatcher getMatcher() {
        return requestMatcher;
    }

    public T getManager() {
        return entry;
    }
}
