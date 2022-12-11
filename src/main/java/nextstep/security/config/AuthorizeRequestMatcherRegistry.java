package nextstep.security.config;

import nextstep.security.access.matcher.RequestMatcher;
import nextstep.security.authentication.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthorizeRequestMatcherRegistry {
    private final Map<RequestMatcher, String> mappings = new HashMap<>();

    public AuthorizedUrl matcher(RequestMatcher requestMatcher) {
        return new AuthorizedUrl(requestMatcher);
    }

    AuthorizeRequestMatcherRegistry addMapping(RequestMatcher requestMatcher, String attributes) {
        mappings.put(requestMatcher, attributes);
        return this;
    }

    public String getAttribute(HttpServletRequest request) {
        for (Map.Entry<RequestMatcher, String> entry : mappings.entrySet()) {
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
        public static final String AUTHORITY = "hasAuthority";

        private final RequestMatcher requestMatcher;

        public AuthorizedUrl(RequestMatcher requestMatcher) {
            this.requestMatcher = requestMatcher;
        }

        public AuthorizeRequestMatcherRegistry permitAll() {
            return access(PERMIT_ALL);
        }

        public AuthorizeRequestMatcherRegistry denyAll() {
            return access(DENY_ALL);
        }

        public AuthorizeRequestMatcherRegistry hasAuthority(String authority) {
            return access(AUTHORITY + "(" + authority + ")");
        }

        public AuthorizeRequestMatcherRegistry authenticated() {
            return access(AUTHENTICATED);
        }

        private AuthorizeRequestMatcherRegistry access(String attribute) {
            return addMapping(requestMatcher, attribute);
        }
    }

    public Boolean isAuthorized(String attribute, Authentication authentication) {
        if (attribute.contains(AuthorizedUrl.AUTHORITY)) {
            Pattern pattern = Pattern.compile("\\((.*?)\\)");
            Matcher matcher = pattern.matcher(attribute);
            if(matcher.find()) {
                String role = matcher.group(1).trim();
                return authentication.getAuthorities().contains(role);
            }
        } else if (attribute.contains(AuthorizedUrl.AUTHENTICATED)) {
            return authentication.isAuthenticated();
        }

        return false;
    }

}
