package nextstep.security.authorization.manager;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;
import nextstep.security.matcher.RequestMatcherEntry;

import java.util.List;

public class RequestMatcherDelegatingAuthorizationManager {

    private final List<RequestMatcherEntry<AuthorizationManager<HttpServletRequest>>> mapping;

    public RequestMatcherDelegatingAuthorizationManager(List<RequestMatcherEntry<AuthorizationManager<HttpServletRequest>>> mapping) {
        this.mapping = mapping;
    }

    public AuthorizationDecision checkInFilter(HttpServletRequest request, Authentication authentication) {
        for (RequestMatcherEntry<AuthorizationManager<HttpServletRequest>> entry : mapping) {
            boolean matches = entry.getMatcher().matches(request);
            if (matches) {
                return entry.getEntry().check(authentication, request);
            }
        }
        return new AuthorizationDecision(false);
    }
}
