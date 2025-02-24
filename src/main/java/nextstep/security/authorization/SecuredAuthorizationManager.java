package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Method;
import java.util.Set;

public class SecuredAuthorizationManager implements AuthorizationManager<MethodInvocation> {

    @Override
    public AuthorizationDecision check(Authentication authentication, MethodInvocation invocation) {
        Method method = invocation.getMethod();
        Class<?> targetClass = invocation.getThis().getClass();
        Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);

        if (specificMethod.isAnnotationPresent(Secured.class)) {
            Secured secured = specificMethod.getAnnotation(Secured.class);

            Set<String> authorities = authentication.getAuthorities();
            if (authorities.contains(secured.value())) {
                return AuthorizationDecision.grantedOf();
            }
        }

        return AuthorizationDecision.deniedOf();
    }
}
