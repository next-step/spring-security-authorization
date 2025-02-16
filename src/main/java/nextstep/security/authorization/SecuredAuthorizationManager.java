package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import org.aopalliance.intercept.MethodInvocation;

public class SecuredAuthorizationManager implements AuthorizationManager<MethodInvocation> {
    @Override
    public AuthorizationDecision check(final Authentication authentication, final MethodInvocation methodInvocation) {
        var method = methodInvocation.getMethod();
        if (!method.isAnnotationPresent(Secured.class)) {
            return AuthorizationDecision.success();

        }
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException();
        }
        var securedAnnotation = method.getAnnotation(Secured.class);
        boolean hasNoAuthority = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .noneMatch(it -> it.equals(securedAnnotation.value()));
        if (hasNoAuthority) {
            return AuthorizationDecision.fail();
        }
        return AuthorizationDecision.success();
    }
}
