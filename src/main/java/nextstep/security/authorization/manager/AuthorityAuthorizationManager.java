package nextstep.security.authorization.manager;

import java.util.Set;
import nextstep.security.authentication.Authentication;

public class AuthorityAuthorizationManager implements AuthorizationManager {

    private final Set<String> authorities;

    public AuthorityAuthorizationManager(Set<String> authorities) {
        this.authorities = authorities;
    }

    public AuthorityAuthorizationManager(String... authorities) {
        this(Set.of(authorities));
    }

    @Override
    public boolean check(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .anyMatch(authorities::contains);
    }
}
