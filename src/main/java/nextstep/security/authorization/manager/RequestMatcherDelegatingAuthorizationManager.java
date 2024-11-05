package nextstep.security.authorization.manager;

import static nextstep.security.authorization.AuthorizationDecision.DENY;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;
import nextstep.security.authorization.matcher.RequestMatcher;
import nextstep.security.authorization.matcher.RequestMatcherEntry;

/**
 * 요청에 따른 AuthorizationManager 을 찾는 역할을 수행.
 * <p>
 * 찾으면 해당 Manager 를 실행하게 한다.
 */
public class RequestMatcherDelegatingAuthorizationManager implements
        AuthorizationManager<HttpServletRequest> {

    private final List<RequestMatcherEntry<AuthorizationManager>> mappings;

    public RequestMatcherDelegatingAuthorizationManager(
            List<RequestMatcherEntry<AuthorizationManager>> mappings) {
        this.mappings = mappings;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest object) {
        for (RequestMatcherEntry<AuthorizationManager> mapping : mappings) {
            RequestMatcher matcher = mapping.getRequestMatcher();
            boolean matchResult = matcher.matches(object);
            if (matchResult) {
                AuthorizationManager manager = mapping.getEntry();
                return manager.check(authentication, object);
            }
        }

        return DENY;
    }
}
