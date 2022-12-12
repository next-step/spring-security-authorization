package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;

public interface RoleManager {

    boolean check(Authentication authentication);
}
