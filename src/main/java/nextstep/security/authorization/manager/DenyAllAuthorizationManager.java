package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;

public class DenyAllAuthorizationManager implements AuthorizationManager {

    @Override
    public boolean check(Authentication authentication) {
        return false;
    }
}
