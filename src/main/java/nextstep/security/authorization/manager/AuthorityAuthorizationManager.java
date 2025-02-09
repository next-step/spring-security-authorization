package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;
import nextstep.security.authorization.ForbiddenException;
import nextstep.security.authorization.Secured;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

public class AuthorityAuthorizationManager implements AuthorizationManager<MethodInvocation> {

    @Override
    public AuthorizationDecision check(Authentication authentication, MethodInvocation methodInvocation) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }

        Method method = methodInvocation.getMethod();

        if (method.isAnnotationPresent(Secured.class)) {
            Secured secured = method.getAnnotation(Secured.class);
            if (!authentication.getAuthorities().contains(secured.value())) {
                throw new ForbiddenException();
            }
        }

        return new AuthorizationDecision(true);
    }
}
