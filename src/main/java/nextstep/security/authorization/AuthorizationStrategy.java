package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

public interface AuthorizationStrategy {

    boolean isGranted(Authentication authentication);
}
