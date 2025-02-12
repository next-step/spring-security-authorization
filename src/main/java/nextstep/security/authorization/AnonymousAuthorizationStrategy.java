package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

public class AnonymousAuthorizationStrategy implements AuthorizationStrategy {

    @Override
    public boolean isGranted(Authentication authentication) {
        return true;
    }
}
