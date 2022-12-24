package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;

public class PermitAllAuthorizationManager implements AuthorizationManager {

    @Override
    public boolean check(Authentication authentication) {
        return true;
    }
}
