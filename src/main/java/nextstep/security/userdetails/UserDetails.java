package nextstep.security.userdetails;

import nextstep.security.authorization.GrantedAuthority;

import java.util.Collection;

public interface UserDetails {
    String getUsername();

    String getPassword();

    Collection<GrantedAuthority> getAuthorities();
}
