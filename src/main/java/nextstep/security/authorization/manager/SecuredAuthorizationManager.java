package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;
import nextstep.security.authorization.Secured;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;

public class SecuredAuthorizationManager implements AuthorizationManager<MethodInvocation> {

    private final AuthorizationManager<Collection<String>> authorizationManager;

    public SecuredAuthorizationManager() {
        this.authorizationManager = new HasAuthorityAuthorizationManager();
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, MethodInvocation invocation) {
        Method method = invocation.getMethod();
        boolean isGranted = isGranted(authentication, method);

        return new AuthorizationDecision(isGranted);
    }

    private boolean isGranted(Authentication authentication, Method method) {
        return (authentication != null && checkAuthorization(authentication, method));
    }

    private boolean checkAuthorization(Authentication authentication, Method method) {
        if (!method.isAnnotationPresent(Secured.class)) {
            return false;
        }

        Secured secured = AnnotationUtils.findAnnotation(method, Secured.class);
        return this.authorizationManager.check(authentication, Set.of(secured.value())).isGranted();
    }
}
