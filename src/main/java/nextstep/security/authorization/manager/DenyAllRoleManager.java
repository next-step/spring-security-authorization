package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;

public class DenyAllRoleManager implements RoleManager {

    @Override
    public boolean check(Authentication authentication) {
        return false;
    }
}
