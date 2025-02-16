package nextstep.security.authorization;

import java.util.Collection;
import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.method.SecuredAuthorizationManager;
import nextstep.security.authorization.web.AuthorityAuthorizationManager;
import nextstep.security.authorization.web.AuthorizationResult;
import nextstep.security.context.SecurityContextHolder;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

public class SecuredMethodInterceptor implements MethodInterceptor, PointcutAdvisor, AopInfrastructureBean {

    private final AuthorizationManager<MethodInvocation> authorizationManager;
    private final Pointcut pointcut;

    public SecuredMethodInterceptor(AuthorityAuthorizationManager<Collection<String>> authorizationManager) {
        this.authorizationManager = new SecuredAuthorizationManager(authorizationManager);
        this.pointcut = new AnnotationMatchingPointcut(null, Secured.class);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthorizationResult authorizationResult = authorizationManager.authorize(authentication, invocation);

        if (!authorizationResult.isGranted()) {
            throw new ForbiddenException();
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
