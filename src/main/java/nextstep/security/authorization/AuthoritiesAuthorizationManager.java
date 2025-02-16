package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;

import java.util.Collection;

public class AuthoritiesAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext<Collection<GrantedAuthority>>> {

    @Override
    public AuthorizationDecision check(final Authentication authentication, final RequestAuthorizationContext<Collection<GrantedAuthority>> context) {
        var authorities = authentication.getAuthorities();
        var matchAuthority = context.getOthers()
                .stream()
                .anyMatch(authorities::contains);
        if (!matchAuthority) {
            return AuthorizationDecision.fail();
        }
        return AuthorizationDecision.success();
    }
}
