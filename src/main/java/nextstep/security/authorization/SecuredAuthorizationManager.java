package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

public class SecuredAuthorizationManager implements AuthorizationManager<MethodInvocation> {

    @Override
    public AuthorizationDecision check(Authentication authentication, MethodInvocation invocation) {
        Method method = invocation.getMethod();
        Secured secured = method.getAnnotation(Secured.class);
        if (authentication == null) {
            return AuthorizationDecision.unAuthorizationDecision();
        }
        if (!authentication.getAuthorities().contains(secured.value())) {
            return AuthorizationDecision.unAuthorizationDecision();
        }

        return AuthorizationDecision.authorizationDecision();
    }
}
