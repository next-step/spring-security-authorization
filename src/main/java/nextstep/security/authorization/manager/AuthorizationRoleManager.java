package nextstep.security.authorization.manager;

import java.util.Set;
import nextstep.security.authentication.Authentication;

public class AuthorizationRoleManager implements RoleManager {

    private final Set<String> authorities;

    public AuthorizationRoleManager(Set<String> authorities) {
        this.authorities = authorities;
    }

    public AuthorizationRoleManager(String... authorities) {
        this(Set.of(authorities));
    }

    @Override
    public boolean check(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .anyMatch(authorities::contains);
    }
}
