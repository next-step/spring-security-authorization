package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Method;

public class SecuredAuthorizationManager implements AuthorizationManager<MethodInvocation> {

    @Override
    public AuthorizationDecision check(Authentication authentication, MethodInvocation invocation) {
        Method method = invocation.getMethod();
        Class<?> targetClass = AopUtils.getTargetClass(method.getClass());
        Method targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
        Secured secured = targetMethod.getAnnotation(Secured.class);

        if (authentication.getAuthorities().contains(secured.value())) {
            return new AuthorizationDecision(true);
        }

        return new AuthorizationDecision(false);
    }
}
