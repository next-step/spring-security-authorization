package nextstep.security.role;

import org.springframework.util.StringUtils;

import java.util.Objects;

public class SimpleGrantedAuthority implements GrantedAuthority {

    private final String role;

    public SimpleGrantedAuthority(String role) {
        if (!StringUtils.hasText(role)) {
            throw new IllegalArgumentException("A granted authority textual representation is required");
        }
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SimpleGrantedAuthority that = (SimpleGrantedAuthority) o;
        return Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(role);
    }
}
