package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.matcher.RequestMatcherEntry;

import java.util.List;

public class RequestMatcherDelegatingAuthorizationManager implements AuthorizationManager<HttpServletRequest> {
    private final List<RequestMatcherEntry<AuthorizationManager<HttpServletRequest>>> mappings;

    public RequestMatcherDelegatingAuthorizationManager(List<RequestMatcherEntry<AuthorizationManager<HttpServletRequest>>> mappings) {
        this.mappings = mappings;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest request) {
        for (RequestMatcherEntry<AuthorizationManager<HttpServletRequest>> entry : mappings) {
            if (entry.matches(request)) {
                return entry.getEntry().check(authentication, request);
            }
        }
        return new AuthorizationDecision(true);
    }
}
