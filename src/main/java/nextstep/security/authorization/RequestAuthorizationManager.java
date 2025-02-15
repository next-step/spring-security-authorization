package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;

import java.util.List;

public class RequestAuthorizationManager implements AuthorizationManager<HttpServletRequest> {

    private final List<RequestMatcherEntry<AuthorizationManager>> requestMatcherEntries;

    public RequestAuthorizationManager(List<RequestMatcherEntry<AuthorizationManager>> requestMatcherEntries) {
        this.requestMatcherEntries = requestMatcherEntries;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest request) {
        return requestMatcherEntries.stream()
                .filter(requestMatcherEntry -> requestMatcherEntry.matchRequest(request))
                .findFirst()
                .map(RequestMatcherEntry::getEntry)
                .map(entry -> entry.check(authentication, request))
                .orElseGet(() -> new AuthorizationDecision(false));
    }
}
