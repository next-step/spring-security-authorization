package nextstep.security.authorization.manager;

import static nextstep.security.authorization.AuthorizationDecision.DENY;

import java.lang.reflect.Method;
import nextstep.app.aspect.Secured;
import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 인증 객체가 있고 인증 관련 어노테이션이 있으면서 인증 관련 어노테이션에 선언된 권한이 인증 객체에 있는지 확인
 */
public class SecuredAuthorizationManager implements AuthorizationManager<MethodInvocation> {

    @Override
    public AuthorizationDecision check(Authentication authentication, MethodInvocation object) {

        // 인증 정보가 없으면 인가 불가
        if (authentication == null) {
            return DENY;
        }

        Method method = object.getMethod();
        Secured secured = method.getAnnotation(Secured.class);

        // Secured 어노테이션이 없으면 인가 불가
        if (secured == null) {
            return DENY;
        }

        // Secured 어노테이션에 선언된 값과 인증 정보의 권한 값이 없는 경우
        // 예시: @Secured("ADMIN") 으로 선언되어있으나 Authority 에 ADMIN 이 없음
        String value = secured.value();
        if (!authentication.getAuthorities().contains(value)) {
            return DENY;
        }

        return new AuthorizationDecision(true);
    }
}
