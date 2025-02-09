package nextstep.security.authorization.manager;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.matcher.RequestMatcherEntry;

import java.util.List;

public class RequestAuthorizationManager implements AuthorizationManager<HttpServletRequest> {
    private final List<RequestMatcherEntry<AuthorizationManager<HttpServletRequest>>> entries;
    private final RequestMatcherEntry<AuthorizationManager<HttpServletRequest>> defaultEntry;

    public RequestAuthorizationManager(
            List<RequestMatcherEntry<AuthorizationManager<HttpServletRequest>>> entries,
            RequestMatcherEntry<AuthorizationManager<HttpServletRequest>> defaultEntry
    ) {
        this.entries = entries;
        this.defaultEntry = defaultEntry;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest target) {
        final boolean isGranted = noneMatch(target)
                ? check(authentication, target, defaultEntry)
                : allMatch(authentication, target);
        return AuthorizationDecision.of(isGranted);
    }

    private boolean noneMatch(HttpServletRequest request) {
        return entries.stream().noneMatch(
                entry -> entry.requestMatcher().matches(request)
        );
    }

    private boolean allMatch(Authentication authentication, HttpServletRequest request) {
        return entries.stream().allMatch(
                entry -> check(authentication, request, entry)
        );
    }

    private boolean check(
            Authentication authentication, HttpServletRequest request,
            RequestMatcherEntry<AuthorizationManager<HttpServletRequest>> matcherEntry
    ) {
        return !matcherEntry.requestMatcher().matches(request)
                || matcherEntry.entry().check(authentication, request).isGranted();
    }
}
