package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;
import nextstep.security.authorization.Secured;
import nextstep.security.role.GrantedAuthority;
import nextstep.security.role.SimpleGrantedAuthority;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class SecuredAuthorizationManager implements AuthorizationManager<MethodInvocation> {

    private final AuthorityAuthorizationManager<Set<GrantedAuthority>> authorityAuthorizationManager;

    public SecuredAuthorizationManager(AuthorityAuthorizationManager<Set<GrantedAuthority>> authorityAuthorizationManager) {
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
            Set<GrantedAuthority> requiredAuthorities = new HashSet<>();
            for (String roleName : secured.value()) {
                requiredAuthorities.add(new SimpleGrantedAuthority(roleName));
            }

            return authorityAuthorizationManager.check(authentication, requiredAuthorities);
        }

        return AuthorizationDecision.granted();
    }
}
