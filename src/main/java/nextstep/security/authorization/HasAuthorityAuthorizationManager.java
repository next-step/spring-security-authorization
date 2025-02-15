package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

public class HasAuthorityAuthorizationManager implements AuthorizationManager {

    private final String authority;

    public HasAuthorityAuthorizationManager(String authority) {
        this.authority = authority;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, Object object) {
        return new AuthorizationDecision(authentication.getAuthorities().contains(authority));
    }
}
