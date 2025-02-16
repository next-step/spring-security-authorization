package nextstep.security.authentication;

import java.util.Collection;
import nextstep.security.core.GrantedAuthority;

public interface Authentication {

    Collection<GrantedAuthority> getAuthorities();

    Object getCredentials();

    Object getPrincipal();

    boolean isAuthenticated();
}
