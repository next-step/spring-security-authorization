package nextstep.security.authentication;

import nextstep.security.authorization.GrantedAuthority;

import java.util.Set;

public interface Authentication {

    Set<GrantedAuthority> getAuthorities();

    Object getCredentials();

    Object getPrincipal();

    boolean isAuthenticated();
}
