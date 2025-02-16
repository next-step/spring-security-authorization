package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;

import java.util.List;

public class RequestAuthorizationManager implements AuthorizationManager<HttpServletRequest> {

    private List<RequestMatcherEntry<AuthorizationManager>> matcherEntries;

    public RequestAuthorizationManager(List<RequestMatcherEntry<AuthorizationManager>> mappings) {
        this.matcherEntries = mappings;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest request) {
        for (RequestMatcherEntry<AuthorizationManager> matcherEntry : matcherEntries) {
            if (matcherEntry.isMatches(request)) {
                return matcherEntry.check(authentication, request);
            }
        }

        return AuthorizationDecision.unAuthorizationDecision();
    }
}
