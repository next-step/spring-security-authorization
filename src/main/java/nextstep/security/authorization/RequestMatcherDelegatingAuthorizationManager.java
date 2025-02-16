package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.requestmatcher.RequestMatcherEntry;

import java.util.Collection;
import java.util.List;

public class RequestMatcherDelegatingAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext<Collection<GrantedAuthority>>> {
    private final List<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext<Collection<GrantedAuthority>>>>> mappings;

    public RequestMatcherDelegatingAuthorizationManager(final List<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext<Collection<GrantedAuthority>>>>> mappings) {
        this.mappings = mappings;
    }

    @Override
    public AuthorizationDecision check(final Authentication authentication, final RequestAuthorizationContext<Collection<GrantedAuthority>> context) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException();
        }
        var allMatch = mappings.stream()
                .filter(it -> it.requestMatcher().matches(context.getRequest()))
                .allMatch(it -> it.entry().check(authentication, context).isSuccess());
        if (!allMatch) {
            return AuthorizationDecision.fail();
        }
        return AuthorizationDecision.success();
    }
}
