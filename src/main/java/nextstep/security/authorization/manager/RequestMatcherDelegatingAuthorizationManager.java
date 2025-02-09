package nextstep.security.authorization.manager;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;
import nextstep.security.matcher.RequestMatcherEntry;

import java.util.List;
import java.util.Objects;

public class RequestMatcherDelegatingAuthorizationManager {

    private final List<RequestMatcherEntry<AuthorizationManager<HttpServletRequest>>> mapping;

    public RequestMatcherDelegatingAuthorizationManager(List<RequestMatcherEntry<AuthorizationManager<HttpServletRequest>>> mapping) {
        this.mapping = mapping;
    }

    public AuthorizationDecision checkInFilter(HttpServletRequest request, Authentication authentication) {
        return mapping.stream()
                .filter(it -> Objects.nonNull(it.getMatcher()))
                .filter(it -> it.getMatcher().matches(request))
                .filter(it -> Objects.nonNull(it.getEntry()))
                .findFirst()
                .map(it -> it.getEntry().check(authentication, request))
                .orElse(AuthorizationDecision.denied());
    }
}
