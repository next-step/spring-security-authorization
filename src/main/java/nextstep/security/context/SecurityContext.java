package nextstep.security.context;

import nextstep.security.authentication.Authentication;
import org.springframework.lang.Nullable;

import java.io.Serializable;

public class SecurityContext implements Serializable {
    private Authentication authentication;

    public SecurityContext() {
    }

    public SecurityContext(Authentication authentication) {
        this.authentication = authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    @Nullable
    public Authentication getAuthentication() {
        return authentication;
    }
}
