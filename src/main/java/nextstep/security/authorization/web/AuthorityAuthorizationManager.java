package nextstep.security.authorization.web;

import java.util.Collection;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.authorization.AuthorizationDecision;
import nextstep.security.authorization.AuthorizationManager;

public class AuthorityAuthorizationManager<T> implements AuthorizationManager<T> {

    private final Collection<String> authorities;

    public AuthorityAuthorizationManager(Collection<String> authorities) {
        this.authorities = authorities;
    }


    @Override
    public AuthorizationDecision check(Authentication authentication, T object) {
        if (authentication == null) {
            throw new AuthenticationException();
        }

        boolean hasAuthority = isAuthorized(authentication, authorities);

        return AuthorizationDecision.of(hasAuthority);
    }


    private boolean isAuthorized(Authentication authentication, Collection<String> authorities) {
        for (String authority : authentication.getAuthorities()) {
            if (authorities.contains(authority)) {
                return true;
            }
        }
        return false;
    }
}
