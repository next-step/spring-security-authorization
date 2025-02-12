package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;
import nextstep.security.authorization.Secured;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.Set;

public class SecuredAuthorizationManager implements AuthorizationManager<MethodInvocation> {

    private final AuthorityAuthorizationManager<Set<String>> authorityAuthorizationManager;

    public SecuredAuthorizationManager(AuthorityAuthorizationManager<Set<String>> authorityAuthorizationManager) {
        this.authorityAuthorizationManager = authorityAuthorizationManager;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, MethodInvocation methodInvocation) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return AuthorizationDecision.denied();
        }

        Method method = methodInvocation.getMethod();

        if (method.isAnnotationPresent(Secured.class)) {
            Secured secured = method.getAnnotation(Secured.class);
            return authorityAuthorizationManager.check(authentication, Set.of(secured.value()));
        }

        return AuthorizationDecision.granted();
    }
}
