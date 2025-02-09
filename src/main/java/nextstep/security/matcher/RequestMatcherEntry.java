package nextstep.security.matcher;


public class RequestMatcherEntry<T> {

    private final RequestMatcher matcher;
    private final T entry;

    public RequestMatcherEntry(RequestMatcher matcher, T entry) {
        this.matcher = matcher;
        this.entry = entry;
    }

    public RequestMatcher getMatcher() {
        return matcher;
    }

    public T getEntry() {
        return entry;
    }
}
