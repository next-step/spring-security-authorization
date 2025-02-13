package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.context.SecurityContextHolder;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

public class AuthorizationManagerBeforeMethodInterceptor implements MethodInterceptor, PointcutAdvisor {

    private final AuthorizationManager<MethodInvocation> authorizationManager;

    public AuthorizationManagerBeforeMethodInterceptor(AuthorizationManager<MethodInvocation> authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthorizationDecision decision = authorizationManager.check(authentication, methodInvocation);

        if (decision != null && !decision.isGranted()) {
            throw new ForbiddenException();
        }

        return methodInvocation.proceed();
    }

    @Override
    public Pointcut getPointcut() {
        return new AnnotationMatchingPointcut(null, Secured.class);
    }

    @Override
    public Advice getAdvice() {
        return this;
    }
}
