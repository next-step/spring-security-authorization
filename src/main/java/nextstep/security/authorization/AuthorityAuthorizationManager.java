package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;

public class AuthorityAuthorizationManager<T> implements AuthorizationManager<T> {
    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";

    private final String authority;

    public AuthorityAuthorizationManager(final String authority) {
        this.authority = authority;
    }

    public static AuthorizationManager<Object> permitAll() {
        return (authentication, object) -> new AuthorizationDecision(true);
    }

    @Override
    public AuthorizationDecision check(final Authentication authentication, final T object) {
        if (authentication == null) {
            throw new AuthenticationException();
        }

        final boolean granted = authentication.getAuthorities().stream()
                .anyMatch(requestAuthority -> authority.isBlank() || requestAuthority.equals(authority));

        return new AuthorizationDecision(granted);
    }
}
