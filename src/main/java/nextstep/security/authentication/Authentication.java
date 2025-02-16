package nextstep.security.authentication;

import nextstep.security.authorization.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

public interface Authentication {

    Collection<GrantedAuthority> getAuthorities();

    Object getCredentials();

    Object getPrincipal();

    boolean isAuthenticated();
}
