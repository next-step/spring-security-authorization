package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

import java.util.Set;

public class AuthorityAuthorizationManager<T> implements AuthorizationManager<T> {

    private final Set<String> authorities;

    public AuthorityAuthorizationManager(String... authorities) {
        this.authorities = Set.of(authorities);
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, T object) {
        for (String authority : this.authorities) {
            if (hasAuthority(authentication, authority)) {
                return AuthorizationDecision.grantedOf();
            }
        }

        return AuthorizationDecision.deniedOf();
    }

    private boolean hasAuthority(Authentication authentication, String authorityToHave) {
        for (String existingAuthority : authentication.getAuthorities()) {
            if (existingAuthority.equals(authorityToHave)) {
                return true;
            }
        }
        return false;
    }
}
