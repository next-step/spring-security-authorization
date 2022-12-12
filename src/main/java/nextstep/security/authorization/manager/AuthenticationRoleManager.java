package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;

public class AuthenticationRoleManager implements RoleManager {

    @Override
    public boolean check(Authentication authentication) {
        return authentication.isAuthenticated();
    }
}
