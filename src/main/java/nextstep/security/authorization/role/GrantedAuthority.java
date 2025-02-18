package nextstep.security.authorization.role;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public interface GrantedAuthority {
    static GrantedAuthority of(String authority) {
        return new DefaultGrantedAuthority(authority);
    }

    static Collection<GrantedAuthority> setOf(Iterable<String> authorities) {
        final Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (String authority : authorities) {
            grantedAuthorities.add(GrantedAuthority.of(authority));
        }
        return grantedAuthorities;
    }

    static Collection<GrantedAuthority> setOf(String... authorities) {
        return setOf(Arrays.asList(authorities));
    }

    String getAuthority();
}
