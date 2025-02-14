package nextstep.security.userdetails;

import java.util.Set;
import nextstep.security.core.GrantedAuthority;

public interface UserDetails {
    String getUsername();

    String getPassword();

    Set<GrantedAuthority> getAuthorities();
}
