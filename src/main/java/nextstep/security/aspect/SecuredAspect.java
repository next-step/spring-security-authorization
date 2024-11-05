package nextstep.security.aspect;

import java.lang.reflect.Method;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.context.SecurityContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class SecuredAspect {

    @Before("@annotation(nextstep.security.authorization.Secured)")
    public void checkSecured(JoinPoint joinPoint) throws NoSuchMethodException {

        // 메소드를 가져온다.
        Method method = getMethodFromJoinPoint(joinPoint);

        // 메소드에 붙어있는 Secured 어노테이션의 value 값을 가져온다. (== ADMIN)
        String secured = method.getAnnotation(Secured.class).value();

        // 인증 객체를 가져온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new AuthenticationException();
        }

        // 인증 객체가 가진 권한이 포함되어 있는지 확인한다.
        // ex) ADMIN 권한이 있는지 체크한다.
        if (!authentication.getAuthorities().contains(secured)) {
            throw new ForbiddenException();
        }
    }

    private Method getMethodFromJoinPoint(JoinPoint joinPoint) throws NoSuchMethodException {
        Class<?> targetClass = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();

        return targetClass.getMethod(methodName, parameterTypes);
    }

}
