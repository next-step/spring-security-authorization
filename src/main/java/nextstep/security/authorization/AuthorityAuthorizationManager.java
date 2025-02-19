package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AuthorityAuthorizationManager<T> implements AuthorizationManager<T> {

    private final Set<String> authorities;

    public AuthorityAuthorizationManager(final String authority, final String... authorities) {
        this.authorities = Stream.concat(
                Stream.of(authority),
                Arrays.stream(authorities)
        ).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public AuthorizationDecision check(final Authentication authentication, final T object) {
        if (authentication == null) {
            throw new AuthenticationException();
        }

        final boolean granted = authentication.getAuthorities().stream()
                .anyMatch(this.authorities::contains);

        return new AuthorizationDecision(granted);
    }
}
