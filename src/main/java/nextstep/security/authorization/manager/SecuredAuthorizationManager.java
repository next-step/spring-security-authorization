package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.Secured;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

public class SecuredAuthorizationManager implements AuthorizationManager<MethodInvocation> {
    private SecuredAuthorizationManager() {}

    public static SecuredAuthorizationManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, MethodInvocation target) {
        return AuthorizationDecision.of(hasAuthority(authentication, target.getMethod()));
    }

    private boolean hasAuthority(Authentication authentication, Method method) {
        return authentication != null
                && authentication.isAuthenticated()
                && method.isAnnotationPresent(Secured.class)
                && authentication.getAuthorities()
                .contains(method.getAnnotation(Secured.class).value());
    }

    private static final class SingletonHolder {
        private static final SecuredAuthorizationManager INSTANCE = new SecuredAuthorizationManager();
    }
}
