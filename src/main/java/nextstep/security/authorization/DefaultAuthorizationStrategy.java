package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

public class DefaultAuthorizationStrategy implements AuthorizationStrategy {

    @Override
    public boolean isGranted(Authentication authentication) {
        if (authentication == null) {
            return false;
        }

        return authentication.isAuthenticated();
    }
}
