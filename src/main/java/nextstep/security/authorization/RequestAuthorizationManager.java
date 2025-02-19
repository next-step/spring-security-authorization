package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.request.RequestMatcher;
import nextstep.security.request.RequestMatcherEntry;

import java.util.List;

public class RequestAuthorizationManager implements AuthorizationManager<HttpServletRequest> {

    private final List<RequestMatcherEntry<AuthorizationManager<HttpServletRequest>>> mappings;

    public RequestAuthorizationManager(final List<RequestMatcherEntry<AuthorizationManager<HttpServletRequest>>> mappings) {
        this.mappings = mappings;
    }

    @Override
    public AuthorizationDecision check(final Authentication authentication, final HttpServletRequest request) {
        return mappings.stream()
                .filter(mapping -> {
                    final RequestMatcher matcher = mapping.getRequestMatcher();
                    return matcher.matches(request);
                }).findFirst()
                .map(mapping -> {
                    final AuthorizationManager<HttpServletRequest> authorizationManager = mapping.getEntry();
                    return authorizationManager.check(authentication, request);
                }).orElse(new AuthorizationDecision(false));
    }
}
