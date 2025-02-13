package nextstep.security.userdetails;

import nextstep.security.role.GrantedAuthority;

import java.util.Set;

public interface UserDetails {
    String getUsername();

    String getPassword();

    Set<GrantedAuthority> getAuthorities();
}
