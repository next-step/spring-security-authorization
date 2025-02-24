package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.context.SecurityContextHolder;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;


/**
 * SecuredMethodInterceptor 역할
 *  - AuthorizationManager를 사용하여 권한 체크. 애노테이션 확인도 AuthorizationManager 에서 진행
 *  - 권한 없을 경우 예외 발생
 *      -> 컨트롤러에서 @Secured를 사용하므로, 이후 DispatcherServlet에서 예외 처리(@ResponseStatus를 통해 응답 처리)
 */
public class SecuredMethodInterceptor implements MethodInterceptor, PointcutAdvisor, AopInfrastructureBean {

    private final Pointcut pointcut;
    private final AuthorizationManager<MethodInvocation> authorizationManager;

    public SecuredMethodInterceptor() {
        this.pointcut = new AnnotationMatchingPointcut(null, Secured.class);
        this.authorizationManager = new SecuredAuthorizationManager();
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        attemptAuthorization(invocation);
        return invocation.proceed();
    }

    private void attemptAuthorization(MethodInvocation invocation) {
        AuthorizationDecision decision = this.authorizationManager.check(getAuthentication(), invocation);
        if (decision == null || decision.denied()) {
            throw new ForbiddenException();
        }
    }

    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException();
        }

        return authentication;
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
