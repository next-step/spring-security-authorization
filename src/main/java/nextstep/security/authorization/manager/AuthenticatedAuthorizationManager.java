package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;

public class AuthenticatedAuthorizationManager implements AuthorizationManager {

    @Override
    public boolean check(Authentication authentication) {
        return authentication.isAuthenticated();
    }
}
