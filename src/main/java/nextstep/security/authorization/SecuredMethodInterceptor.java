package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.context.SecurityContextHolder;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

import java.lang.reflect.Method;

public class SecuredMethodInterceptor implements MethodInterceptor, PointcutAdvisor, AopInfrastructureBean {

    private final Pointcut pointcut;
    private final AuthorizationManager<MethodInvocation> authorizationManager;

    public SecuredMethodInterceptor(AuthorizationManager<MethodInvocation> authorizationManager) {
        this.pointcut = new AnnotationMatchingPointcut(null, Secured.class);
        this.authorizationManager = authorizationManager;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        if (method.isAnnotationPresent(Secured.class)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AuthorizationDecision authorizationDecision = authorizationManager.check(authentication,invocation);

            if (!authorizationDecision.result) {
                throw new ForbiddenException();
            }
        }
        return invocation.proceed();
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this;
    }

    @Override
    public boolean isPerInstance() {
        return true;
    }
}
