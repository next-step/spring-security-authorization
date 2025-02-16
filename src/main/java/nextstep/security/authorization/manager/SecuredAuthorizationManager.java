package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.Secured;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

public class SecuredAuthorizationManager implements AuthorizationManager<MethodInvocation> {

    @Override
    public AuthorizationResult authorize(Authentication authentication, MethodInvocation target) {
        return AuthorizationDecision.from(hasAuthority(authentication, target.getMethod()));
    }

    private boolean hasAuthority(Authentication authentication, Method method) {
        if (!method.isAnnotationPresent(Secured.class)) {
            return false;
        }
        return authentication != null
                && authentication.isAuthenticated()
                && authentication.getAuthorities()
                .contains(method.getAnnotation(Secured.class).value());
    }
}
