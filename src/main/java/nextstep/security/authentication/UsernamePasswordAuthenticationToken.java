package nextstep.security.authentication;

import nextstep.security.authorization.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

public class UsernamePasswordAuthenticationToken implements Authentication {

    private final Object principal;
    private final Object credentials;
    private final boolean authenticated;
    private final Collection<GrantedAuthority> authorities;

    private UsernamePasswordAuthenticationToken(Object principal, Object credentials, boolean authenticated, Collection<GrantedAuthority> authorities) {
        this.principal = principal;
        this.credentials = credentials;
        this.authenticated = authenticated;
        this.authorities = authorities;
    }

    public static UsernamePasswordAuthenticationToken unauthenticated(String principal, String credentials) {
        return new UsernamePasswordAuthenticationToken(principal, credentials, false, Set.of());
    }


    public static UsernamePasswordAuthenticationToken authenticated(String principal, String credentials, Collection<GrantedAuthority> authorities) {
        return new UsernamePasswordAuthenticationToken(principal, credentials, true, authorities);
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
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
