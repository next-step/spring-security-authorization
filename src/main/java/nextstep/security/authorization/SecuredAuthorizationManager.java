package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.authorization.hierarchy.RoleHierarchy;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class SecuredAuthorizationManager implements AuthorizationManager<MethodInvocation> {
    private final AuthoritiesAuthorizationManager authoritiesAuthorizationManager;

    public SecuredAuthorizationManager(final RoleHierarchy roleHierarchy) {
        this.authoritiesAuthorizationManager = new AuthoritiesAuthorizationManager(roleHierarchy);
    }

    @Override
    public AuthorizationDecision check(final Authentication authentication, final MethodInvocation invocation) {
        return extractSecuredAnnotation(invocation)
                .map(secured -> authorize(authentication, secured))
                .orElseGet(() -> new AuthorizationDecision(false));
    }

    private Optional<Secured> extractSecuredAnnotation(MethodInvocation invocation) {
        Method method = AopUtils.getMostSpecificMethod(invocation.getMethod(), invocation.getThis().getClass());
        return Optional.ofNullable(method.getAnnotation(Secured.class));
    }

    private AuthorizationDecision authorize(Authentication authentication, Secured secured) {
        if (isAuthenticationInValid(authentication)) {
            throw new AuthenticationException();
        }

        return authoritiesAuthorizationManager.check(authentication, List.of(secured.value()));
    }

    private boolean isAuthenticationInValid(Authentication authentication) {
        return authentication == null;
    }
}
