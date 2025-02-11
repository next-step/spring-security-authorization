package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;

import java.util.Collection;
import java.util.Set;

public class HasAuthorityAuthorizationManager<T> implements AuthorizationManager<T> {

    private final AuthorizationManager<Collection<String>> authorizationManager;
    private final Set<String> authorities;

    public HasAuthorityAuthorizationManager(String... authorities) {
        this.authorizationManager = new AuthoritiesAuthorizationManager();
        this.authorities = Set.of(authorities);
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, T object) {
        return this.authorizationManager.check(authentication, authorities);
    }
}
