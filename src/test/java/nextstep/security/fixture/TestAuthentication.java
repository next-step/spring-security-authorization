package nextstep.security.fixture;

import nextstep.security.authentication.Authentication;

import java.util.Set;
import java.util.UUID;

public class TestAuthentication implements Authentication {
    private final String username = UUID.randomUUID().toString();
    private final String password = UUID.randomUUID().toString();
    private final Set<String> authorities;
    private final boolean isAuthenticated;

    public TestAuthentication(Set<String> authorities, boolean isAuthenticated) {
        this.authorities = authorities;
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public Set<String> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public static Authentication admin() {
        return new TestAuthentication(Set.of("ADMIN"), true);
    }

    public static Authentication user() {
        return new TestAuthentication(Set.of("USER"), true);
    }

    public static Authentication unAuthenticated() {
        return new TestAuthentication(Set.of(), false);
    }

    public static Authentication authenticated() {
        return new TestAuthentication(Set.of(), true);
    }
}
