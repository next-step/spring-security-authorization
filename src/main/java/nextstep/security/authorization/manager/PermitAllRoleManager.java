package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;

public class PermitAllRoleManager implements RoleManager {

    @Override
    public boolean check(Authentication authentication) {
        return true;
    }
}
