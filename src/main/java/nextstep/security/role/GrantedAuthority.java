package nextstep.security.role;

import java.io.Serializable;

public interface GrantedAuthority extends Serializable {
	String getAuthority();
}
