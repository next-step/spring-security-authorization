package nextstep.security.authorization.method;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.authorization.AuthorizationDecision;
import nextstep.security.authorization.AuthorizationManager;
import nextstep.security.authorization.Secured;
import org.aopalliance.intercept.MethodInvocation;

public class SecuredAuthorizationManager implements AuthorizationManager<MethodInvocation> {
    @Override
    public AuthorizationDecision check(Authentication authentication, MethodInvocation invocation) {
        if (authentication == null) {
            throw new AuthenticationException();
        }

        Set<String> authorities = getAuthorities(invocation);

        boolean hasAuthority = authentication.getAuthorities().stream()
                .anyMatch(authorities::contains);

        return AuthorizationDecision.of(hasAuthority);
    }

    private Set<String> getAuthorities(MethodInvocation invocation) {
        Method method = invocation.getMethod();

        if (!method.isAnnotationPresent(Secured.class)) {
            return Collections.emptySet();
        }

        Secured secured = method.getAnnotation(Secured.class);
        return Set.of(secured.value());
    }
}
