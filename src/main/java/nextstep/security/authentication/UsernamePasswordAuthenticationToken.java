package nextstep.security.authentication;

import nextstep.security.SimpleGrantedAuthority;
import nextstep.security.authorization.GrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public class UsernamePasswordAuthenticationToken implements Authentication {

    private final Object principal;
    private final Object credentials;
    private final boolean authenticated;
    private final Set<GrantedAuthority> authorities;

    private UsernamePasswordAuthenticationToken(Object principal, Object credentials, boolean authenticated,
                                                Set<String> authorities) {
        this.principal = principal;
        this.credentials = credentials;
        this.authenticated = authenticated;
        this.authorities = authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    public static UsernamePasswordAuthenticationToken unauthenticated(String principal, String credentials) {
        return new UsernamePasswordAuthenticationToken(principal, credentials, false, Set.of());
    }


    public static UsernamePasswordAuthenticationToken authenticated(String principal, String credentials, Set<String> authorities) {
        return new UsernamePasswordAuthenticationToken(principal, credentials, true, authorities);
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }
}
