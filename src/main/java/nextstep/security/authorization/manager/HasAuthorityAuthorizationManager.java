package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;

import java.util.Collection;

public class HasAuthorityAuthorizationManager implements AuthorizationManager<Collection<String>> {

    @Override
    public AuthorizationDecision check(Authentication authentication, Collection<String> authorities) {

        boolean granted = isGranted(authentication, authorities);
        return new AuthorizationDecision(granted);
    }

    private boolean isGranted(Authentication authentication, Collection<String> authorities) {
        return (authentication != null) && hasAuthority(authentication, authorities);
    }

    private boolean hasAuthority(Authentication authentication, Collection<String> authorities) {
        for (String authority : authentication.getAuthorities()) {
            if (authorities.contains(authority)) {
                return true;
            }
        }

        return false;
    }
}
