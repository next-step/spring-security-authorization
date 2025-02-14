package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class SecuredAuthorizationManager implements AuthorizationManager<MethodInvocation> {

    private final AuthorityAuthorizationManager<Collection<String>> authoritiesAuthorizationManager;

    public SecuredAuthorizationManager(AuthorityAuthorizationManager<Collection<String>> authoritiesAuthorizationManager) {
        this.authoritiesAuthorizationManager = authoritiesAuthorizationManager;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, MethodInvocation methodInvocation) {

        Set<String> authorities = getAuthorities(methodInvocation);

        return authoritiesAuthorizationManager.check(authentication, authorities);
    }


    private Set<String> getAuthorities(MethodInvocation methodInvocation) {

        Method method = methodInvocation.getMethod();
        Object target = methodInvocation.getThis();
        Class<?> targetClass = target != null ? target.getClass() : null;

        return resolveAuthorities(method, targetClass);
    }

    private Set<String> resolveAuthorities(Method method, Class<?> targetClass) {
        Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
        Secured secured = findSecuredAnnotation(specificMethod);
        return secured != null ? Set.of(secured.value()) : Collections.emptySet();
    }

    private Secured findSecuredAnnotation(Method method) {
        return method.getAnnotation(Secured.class);
    }
}
