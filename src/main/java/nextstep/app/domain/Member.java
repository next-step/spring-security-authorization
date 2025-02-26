package nextstep.app.domain;

import nextstep.security.authorization.GrantedAuthority;

import java.util.Set;

public class Member {
    private final String email;
    private final String password;
    private final String name;
    private final String imageUrl;
    private final Set<GrantedAuthority> roles;

    public Member(String email, String password, String name, String imageUrl, Set<GrantedAuthority> roles) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.imageUrl = imageUrl;
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Set<GrantedAuthority> getRoles() {
        return roles;
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }
}
