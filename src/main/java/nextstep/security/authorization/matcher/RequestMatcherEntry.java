package nextstep.security.authorization.matcher;

public record RequestMatcherEntry<T>(
        RequestMatcher requestMatcher,
        T entry
) {}
