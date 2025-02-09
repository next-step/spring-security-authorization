package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.Set;

public class SecuredAuthorizationManager implements AuthorizationManager<MethodInterceptor> {

    @Override
    public AuthorizationDecision check(Authentication authentication, MethodInterceptor methodInterceptor) {
        if (authentication == null) {
            return new AuthorizationDecision(false);
        }


        Set<String> authorities = authentication.getAuthorities();
        if (authorities.stream().noneMatch("ADMIN"::equalsIgnoreCase)) {
            return new AuthorizationDecision(false);
        }

        return new AuthorizationDecision(true);
    }
}
