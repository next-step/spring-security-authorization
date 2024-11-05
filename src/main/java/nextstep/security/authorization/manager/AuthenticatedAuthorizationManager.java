package nextstep.security.authorization.manager;

import static nextstep.security.authorization.AuthorizationDecision.DENY;

import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;

/**
 * 인증 객체만 있으면 통과
 */
public class AuthenticatedAuthorizationManager implements AuthorizationManager {

    @Override
    public AuthorizationDecision check(Authentication authentication, Object object) {
        if (authentication == null) {
            return DENY;
        }
        return new AuthorizationDecision(true);
    }
}
