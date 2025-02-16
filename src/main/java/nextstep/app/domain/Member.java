package nextstep.app.domain;

import java.util.Set;

public class Member {
    private String email;
    private String password;
    private String name;
    private String imageUrl;
    private Set<String> roles;

    private Member() {
    }

    public Member(String email, String password, String name, String imageUrl, Set<String> roles) {
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

    public Set<String> getRoles() {
        return roles;
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }
}
