package nextstep.security.config;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import nextstep.security.access.matcher.RequestMatcher;
import nextstep.security.authorization.manager.AuthenticationRoleManager;
import nextstep.security.authorization.manager.AuthorizationRoleManager;
import nextstep.security.authorization.manager.DenyAllRoleManager;
import nextstep.security.authorization.manager.PermitAllRoleManager;
import nextstep.security.authorization.manager.RoleManager;

public class AuthorizeRequestMatcherRegistry {
    private final Map<RequestMatcher, RoleManager> mappings = new HashMap<>();

    public AuthorizedUrl matcher(RequestMatcher requestMatcher) {
        return new AuthorizedUrl(requestMatcher);
    }

    AuthorizeRequestMatcherRegistry addMapping(RequestMatcher requestMatcher, RoleManager roleManager) {
        mappings.put(requestMatcher, roleManager);
        return this;
    }

    public RoleManager getRoleManager(HttpServletRequest request) {
        for (var entry : mappings.entrySet()) {
            if (entry.getKey().matches(request)) {
                return entry.getValue();
            }
        }

        return null;
    }

    public class AuthorizedUrl {
        public static final String PERMIT_ALL = "permitAll";
        public static final String DENY_ALL = "denyAll";
        public static final String AUTHENTICATED = "authenticated";
        private final RequestMatcher requestMatcher;

        public AuthorizedUrl(RequestMatcher requestMatcher) {
            this.requestMatcher = requestMatcher;
        }

        public AuthorizeRequestMatcherRegistry permitAll() {
            return access(new PermitAllRoleManager());
        }

        public AuthorizeRequestMatcherRegistry denyAll() {
            return access(new DenyAllRoleManager());
        }

        public AuthorizeRequestMatcherRegistry hasAuthority(String... authorities) {
            return access(new AuthorizationRoleManager(authorities));
        }

        public AuthorizeRequestMatcherRegistry authenticated() {
            return access(new AuthenticationRoleManager());
        }

        private AuthorizeRequestMatcherRegistry access(RoleManager roleManager) {
            return addMapping(requestMatcher, roleManager);
        }
    }

}
