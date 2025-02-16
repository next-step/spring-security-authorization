package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.authorization.manager.SecuredAuthorizationManager;
import nextstep.security.context.SecurityContextHolder;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

public class SecuredMethodInterceptor implements MethodInterceptor, PointcutAdvisor, AopInfrastructureBean {

    private final SecuredAuthorizationManager securedAuthorizationManager;

    private final Pointcut pointcut;

    public SecuredMethodInterceptor() {
        this.pointcut = new AnnotationMatchingPointcut(null, Secured.class);
        this.securedAuthorizationManager = new SecuredAuthorizationManager();
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (!invocation.getMethod().isAnnotationPresent(Secured.class)) {
            return invocation.proceed();
        }
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        checkAuthenticated(authentication);
        checkAuthorize(authentication, invocation);

        return invocation.proceed();
    }

    private void checkAuthenticated(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException();
        }
    }

    private void checkAuthorize(Authentication authentication, MethodInvocation invocation) {
        if (!securedAuthorizationManager.authorize(authentication, invocation).isGranted()) {
            throw new ForbiddenException();
        }
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
