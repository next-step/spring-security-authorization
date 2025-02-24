package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.util.matcher.RequestMatcherEntry;

import java.util.List;

/**
 * - 요청에 대해서 적합한 권한이 있는지만 판단.
 * - 인가가 되지 않았을 경우 예외를 발생시키지는 않는다. (예외는 AuthorizationFilter에서 던짐)
 */
public class RequestAuthorizationManager implements AuthorizationManager<HttpServletRequest> {

    private final List<RequestMatcherEntry<AuthorizationManager>> mappings;

    public RequestAuthorizationManager(List<RequestMatcherEntry<AuthorizationManager>> mappings) {
        this.mappings = mappings;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest request) {
        if (!isAuthenticated(authentication)) {
            return AuthorizationDecision.deniedOf();
        }

        for (RequestMatcherEntry<AuthorizationManager> entry : mappings) {
            if (entry.matches(request)) {
                AuthorizationManager manager = entry.getEntry();
                return manager.check(authentication, request);
            }
        }

        return AuthorizationDecision.deniedOf();
    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }
}
