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
        if (noneMatch(target)) {
            return AuthorizationDecision.of(
                    check(authentication, target, defaultEntry)
            );
        }
        return AuthorizationDecision.of(
                allMatch(authentication, target)
        );
    }

    private boolean noneMatch(HttpServletRequest request) {
        for (var entry : entries) {
            if (entry.requestMatcher().matches(request)) {
                return false;
            }
        }
        return true;
    }

    private boolean allMatch(Authentication authentication, HttpServletRequest request) {
        for (var entry : entries) {
            if (!check(authentication, request, entry)) {
                return false;
            }
        }
        return true;
    }

    private boolean check(
            Authentication authentication, HttpServletRequest request,
            RequestMatcherEntry<AuthorizationManager<HttpServletRequest>> matcherEntry
    ) {
        return !matcherEntry.requestMatcher().matches(request)
                || matcherEntry.entry().check(authentication, request).isGranted();
    }
}
