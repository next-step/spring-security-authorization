package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;

public interface AuthorizationManager {

    boolean check(Authentication authentication);
}
