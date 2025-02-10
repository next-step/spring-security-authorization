package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.authorization.AuthorizationDecision;
import nextstep.security.authorization.ForbiddenException;
import nextstep.security.authorization.Secured;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;

public class SecuredAuthorizationManager implements AuthorizationManager<MethodInvocation> {

    private final AuthorizationManager<Collection<String>> authorizationManager;

    public SecuredAuthorizationManager() {
        this.authorizationManager = new AuthoritiesAuthorizationManager();
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, MethodInvocation invocation) {
        Method method = invocation.getMethod();

        if (authentication == null) {
            throw new ForbiddenException();
        }

        if (method.isAnnotationPresent(Secured.class)) {
            Secured secured = getSecured(method);
            return this.authorizationManager.check(authentication, Set.of(secured.value()));
        }

        return AuthorizationDecision.ACCESS_DENIED;
    }

    private Secured getSecured(Method method) {
        Secured secured = AnnotationUtils.findAnnotation(method, Secured.class);
        if (secured == null) {
            throw new IllegalStateException();
        }

        return secured;
    }
}
