package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class AuthorityAuthorizationManager implements AuthorizationManager<HttpServletRequest> {
    private final Set<String> defined_authorities = new HashSet<>();

    public AuthorityAuthorizationManager(String... authorities) {
        Collections.addAll(this.defined_authorities, authorities);
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest object) {
        if (Objects.isNull(authentication)) {
            return new AuthorizationDecision(false);
        }
        Set<String> authorities = authentication.getAuthorities();

        if (noAuthority(authorities, this.defined_authorities)) {
            return new AuthorizationDecision(false);
        }
        return new AuthorizationDecision(true);
    }

    public boolean noAuthority(Set<String> authorities, Set<String> myAuthorities) {
        return Collections.disjoint(authorities, myAuthorities);
    }
}
