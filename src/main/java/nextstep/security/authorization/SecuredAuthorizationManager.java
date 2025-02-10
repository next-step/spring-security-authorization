package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.List;

public class SecuredAuthorizationManager implements AuthorizationManager<MethodInvocation> {
    private final AuthoritiesAuthorizationManager authoritiesAuthorizationManager = new AuthoritiesAuthorizationManager();

    @Override
    public AuthorizationDecision check(final Authentication authentication, final MethodInvocation invocation) {
        Method method = invocation.getMethod();

        if (method.isAnnotationPresent(Secured.class)) {
            Secured secured = method.getAnnotation(Secured.class);

            if (authentication == null) {
                throw new AuthenticationException();
            }

            return authoritiesAuthorizationManager.check(authentication, List.of(secured.value()));
        }

        return new AuthorizationDecision(true);
    }
}
