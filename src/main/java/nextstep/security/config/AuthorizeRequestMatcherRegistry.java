package nextstep.security.config;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import nextstep.security.access.matcher.RequestMatcher;
import nextstep.security.authorization.manager.AuthenticatedAuthorizationManager;
import nextstep.security.authorization.manager.AuthorizationManager;
import nextstep.security.authorization.manager.AuthorityAuthorizationManager;
import nextstep.security.authorization.manager.DenyAllAuthorizationManager;
import nextstep.security.authorization.manager.PermitAllAuthorizationManager;

public class AuthorizeRequestMatcherRegistry {
    private final Map<RequestMatcher, AuthorizationManager> mappings = new HashMap<>();

    public AuthorizedUrl matcher(RequestMatcher requestMatcher) {
        return new AuthorizedUrl(requestMatcher);
    }

    AuthorizeRequestMatcherRegistry addMapping(RequestMatcher requestMatcher, AuthorizationManager authorizationManager) {
        mappings.put(requestMatcher, authorizationManager);
        return this;
    }

    public AuthorizationManager getAuthorizationManager(HttpServletRequest request) {
        for (var entry : mappings.entrySet()) {
            if (entry.getKey().matches(request)) {
                return entry.getValue();
            }
        }

        return null;
    }

    public class AuthorizedUrl {
        private final RequestMatcher requestMatcher;

        public AuthorizedUrl(RequestMatcher requestMatcher) {
            this.requestMatcher = requestMatcher;
        }

        public AuthorizeRequestMatcherRegistry permitAll() {
            return access(new PermitAllAuthorizationManager());
        }

        public AuthorizeRequestMatcherRegistry denyAll() {
            return access(new DenyAllAuthorizationManager());
        }

        public AuthorizeRequestMatcherRegistry hasAuthority(String... authorities) {
            return access(new AuthorityAuthorizationManager(authorities));
        }

        public AuthorizeRequestMatcherRegistry authenticated() {
            return access(new AuthenticatedAuthorizationManager());
        }

        private AuthorizeRequestMatcherRegistry access(AuthorizationManager authorizationManager) {
            return addMapping(requestMatcher, authorizationManager);
        }
    }

}
