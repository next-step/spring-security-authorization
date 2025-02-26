package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.requestmatcher.RequestMatcherEntry;

import java.util.List;

public class RequestMatcherDelegatingAuthorizationManager implements AuthorizationManager<HttpServletRequest> {
    private final List<RequestMatcherEntry<AuthorizationManager<HttpServletRequest>>> mappings;

    public RequestMatcherDelegatingAuthorizationManager(final List<RequestMatcherEntry<AuthorizationManager<HttpServletRequest>>> mappings) {
        this.mappings = mappings;
    }

    @Override
    public AuthorizationDecision check(final Authentication authentication, final HttpServletRequest request) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException();
        }
        var allMatch = mappings.stream()
                .filter(it -> it.requestMatcher().matches(request))
                .allMatch(it -> it.entry().check(authentication, request).isSuccess());
        if (!allMatch) {
            return AuthorizationDecision.fail();
        }
        return AuthorizationDecision.success();
    }
}
