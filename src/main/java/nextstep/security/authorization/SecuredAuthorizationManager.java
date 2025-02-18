package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Method;

public class SecuredAuthorizationManager implements AuthorizationManager<MethodInvocation> {
    @Override
    public AuthorizationDecision check(final Authentication authentication, final MethodInvocation methodInvocation) {
        if (authentication == null) {
            return new AuthorizationDecision(false);
        }

        final String authorities = getAuthorities(methodInvocation);

        final boolean hasNotRequiredRole = authentication.getAuthorities().stream()
                .noneMatch(role -> role.equals(authorities));

        if (hasNotRequiredRole) {
            return new AuthorizationDecision(false);
        }

        return new AuthorizationDecision(true);
    }

    private String getAuthorities(final MethodInvocation methodInvocation) {
        final Method method = methodInvocation.getMethod();
        final Object target = methodInvocation.getThis();

        if (target == null) {
            return null;
        }

        final Class<?> clazz = target.getClass();

        final Method specificMethod = AopUtils.getMostSpecificMethod(method, clazz);
        final Secured secured = specificMethod.getAnnotation(Secured.class);

        if (secured == null) {
            return null;
        }

        return secured.value();
    }

}
