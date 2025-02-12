package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.Objects;

public class SecuredAuthorizationManager implements AuthorizationManager<MethodInvocation> {
    @Override
    public AuthorizationDecision check(Authentication authentication, MethodInvocation invocation) {
        Method method = invocation.getMethod();
        if (method.isAnnotationPresent(Secured.class)) {
            Secured secured = method.getAnnotation(Secured.class);

            if (Objects.isNull(authentication)
                    || !authentication.getAuthorities().contains(secured.value())) {
                return new AuthorizationDecision(false);
            }
        }
        return new AuthorizationDecision(true);
    }
}
