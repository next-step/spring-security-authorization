package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

import java.util.Collection;

public class AuthoritiesAuthorizationManager implements AuthorizationManager<Collection<String>> {

    @Override
    public AuthorizationDecision check(final Authentication authentication, final Collection<String> authorities) {
        return new AuthorizationDecision(isGranted(authentication, authorities));
    }

    private boolean isGranted(Authentication authentication, Collection<String> authorities) {
        return authentication != null && isAuthorized(authentication, authorities);
    }

    private boolean isAuthorized(Authentication authentication, Collection<String> authorities) {
        for (String grantedAuthority : authentication.getAuthorities()) {
            if (authorities.contains(grantedAuthority)) {
                return true;
            }
        }

        return false;
    }
}
