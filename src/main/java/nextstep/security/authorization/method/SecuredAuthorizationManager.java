package nextstep.security.authorization.method;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;
import nextstep.security.authorization.AuthorizationManager;
import nextstep.security.authorization.Secured;
import nextstep.security.authorization.web.AuthorityAuthorizationManager;
import org.aopalliance.intercept.MethodInvocation;

public class SecuredAuthorizationManager implements AuthorizationManager<MethodInvocation> {

    private AuthorityAuthorizationManager<Collection<String>> authorityAuthorizationManager;

    public void setAuthorityAuthorizationManager(Collection<String> authorities) {
        authorityAuthorizationManager = new AuthorityAuthorizationManager<>(authorities);
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, MethodInvocation invocation) {
        Collection<String> authorities = getAuthorities(invocation);

        if (authorities.isEmpty()) {
            return null;
        }
        setAuthorityAuthorizationManager(authorities);
        return authorities.isEmpty() ? null : authorityAuthorizationManager.check(authentication, authorities);
    }

    private Collection<String> getAuthorities(MethodInvocation invocation) {
        Method method = invocation.getMethod();

        if (!method.isAnnotationPresent(Secured.class)) {
            return Collections.emptySet();
        }

        Secured secured = method.getAnnotation(Secured.class);
        return Set.of(secured.value());
    }
}
