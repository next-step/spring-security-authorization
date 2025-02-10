package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.matcher.RequestMatcher;
import nextstep.security.authorization.matcher.RequestMatcherEntry;

import java.util.List;

public class RequestAuthorizationManager implements AuthorizationManager<HttpServletRequest> {
    private final List<RequestMatcherEntry<AuthorizationManager>> mappings;

    public RequestAuthorizationManager(final List<RequestMatcherEntry<AuthorizationManager>> mappings) {
        this.mappings = mappings;
    }

    @Override
    public AuthorizationDecision check(final Authentication authentication, final HttpServletRequest request) {
        for (RequestMatcherEntry<AuthorizationManager> mapping : this.mappings) {
            RequestMatcher matcher = mapping.getRequestMatcher();
            if (matcher.matches(request)) {
                AuthorizationManager manager = mapping.getEntry();

                return manager.check(authentication, request);
            }
        }

        return new AuthorizationDecision(false);
    }
}
