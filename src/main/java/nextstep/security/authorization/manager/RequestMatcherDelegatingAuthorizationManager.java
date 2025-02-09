package nextstep.security.authorization.manager;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;
import nextstep.security.matcher.RequestMatcherEntry;
import org.aopalliance.intercept.MethodInvocation;

import java.util.List;

public class RequestMatcherDelegatingAuthorizationManager {

    private final List<RequestMatcherEntry<AuthorizationManager>> mapping;

    public RequestMatcherDelegatingAuthorizationManager(List<RequestMatcherEntry<AuthorizationManager>> mapping) {
        this.mapping = mapping;
    }

    public AuthorizationDecision checkInFilter(HttpServletRequest request, Authentication authentication) {
        for (RequestMatcherEntry<AuthorizationManager> entry : mapping) {
            boolean matches = entry.getMatcher().matches(request);
            if (matches) {
                AuthorizationManager authorizationManager = entry.getEntry();
                if (authorizationManager instanceof AuthorityAuthorizationManager) {
                    return new AuthorizationDecision(true);
                }
                return authorizationManager.check(authentication, request);
            }
        }
        return new AuthorizationDecision(false);
    }

    public AuthorizationDecision checkMethod(Authentication authentication, MethodInvocation methodInvocation) {
        AuthorizationManager manager = mapping.stream()
                .map(RequestMatcherEntry::getEntry)
                .filter(AuthorityAuthorizationManager.class::isInstance)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No matching authorization manager"));

        return manager.check(authentication, methodInvocation);
    }
}
