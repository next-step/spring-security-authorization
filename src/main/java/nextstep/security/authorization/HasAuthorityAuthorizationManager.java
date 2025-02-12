package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

public class HasAuthorityAuthorizationManager implements AuthorizationManager {
    private final String allowRole;

    public HasAuthorityAuthorizationManager(String allowRole) {
        this.allowRole = allowRole;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, Object object) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return AuthorizationDecision.deny();
        }

        if (authentication.getAuthorities().contains(allowRole)) {
            return AuthorizationDecision.granted();
        }

        return AuthorizationDecision.deny();
    }
}
