package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;

public class AuthorityAuthorizationManager<T> implements AuthorizationManager<T> {
    private final String authority;

    public AuthorityAuthorizationManager(String authority) {
        this.authority = authority;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, T object) {
        if (authentication == null) {
            throw new AuthenticationException();
        }

        boolean isGranted = authentication.getAuthorities().stream()
                .anyMatch(requestAuthority -> {
                    if (authority.equals("ADMIN")) {
                        return "ADMIN".equals(requestAuthority);
                    } else if (authority.equals("USER")) {
                        return "ADMIN".equals(requestAuthority) || "USER".equals(requestAuthority);
                    }
                    return false;
                });

        return new AuthorizationDecision(isGranted);
    }
}
