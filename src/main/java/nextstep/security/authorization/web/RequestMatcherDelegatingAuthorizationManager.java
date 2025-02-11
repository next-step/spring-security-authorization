package nextstep.security.authorization.web;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;
import nextstep.security.authorization.AuthorizationManager;
import nextstep.security.util.RequestMatcher;
import nextstep.security.util.RequestMatcherEntry;

public class RequestMatcherDelegatingAuthorizationManager implements AuthorizationManager<HttpServletRequest> {

    private static final AuthorizationDecision DENY = new AuthorizationDecision(false);

    private final List<RequestMatcherEntry<AuthorizationManager>> mappings;

    public RequestMatcherDelegatingAuthorizationManager(List<RequestMatcherEntry<AuthorizationManager>> mappings) {
        this.mappings = mappings;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest request) {
        for (RequestMatcherEntry<AuthorizationManager> mapping : mappings) {
            if (mapping.getRequestMatcher().matches(request)) {
                return mapping.getEntry().check(authentication, request);
            }
        }
        return DENY;
    }
}
