package nextstep.security.authorization.manager;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;
import nextstep.security.authorization.matcher.RequestMatcherEntry;

import java.util.List;

public class RequestMatcherDelegatingAuthorizationManager implements AuthorizationManager<HttpServletRequest> {

    private final List<RequestMatcherEntry<AuthorizationManager<HttpServletRequest>>> mappings;

    public RequestMatcherDelegatingAuthorizationManager(List<RequestMatcherEntry<AuthorizationManager<HttpServletRequest>>> mappings) {
        this.mappings = mappings;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest httpServletRequest) {
        for (RequestMatcherEntry<AuthorizationManager<HttpServletRequest>> entry : mappings) {
            if (entry.getMatcher().matches(httpServletRequest)) {
                return entry.getManager().check(authentication, httpServletRequest);
            }
        }

        return AuthorizationDecision.ACCESS_DENIED;
    }
}
