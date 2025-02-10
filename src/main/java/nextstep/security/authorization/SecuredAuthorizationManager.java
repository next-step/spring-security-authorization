package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

public class SecuredAuthorizationManager implements AuthorizationManager<MethodInvocation> {
    @Override
    public AuthorizationDecision check(final Authentication authentication, final MethodInvocation invocation) {
        Method method = invocation.getMethod();
        if (method.isAnnotationPresent(Secured.class)) {
            Secured secured = method.getAnnotation(Secured.class);
            if (authentication == null) {
                throw new AuthenticationException();
            }

            if (!authentication.getAuthorities().contains(secured.value())) {
                throw new ForbiddenException();
            }
        }

        return new AuthorizationDecision(true);
    }
}
