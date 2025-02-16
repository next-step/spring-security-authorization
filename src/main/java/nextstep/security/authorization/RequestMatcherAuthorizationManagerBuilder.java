package nextstep.security.authorization;

import nextstep.security.matcher.MvcRequestMatcher;
import nextstep.security.matcher.RequestMatcher;
import nextstep.security.matcher.RequestMatcherEntry;

import java.util.ArrayList;
import java.util.List;

public class RequestMatcherAuthorizationManagerBuilder {
    private List<RequestMatcherEntry<AuthorizationManager>> mappings = new ArrayList<>();
    private final RoleHierarchy roleHierarchy;

    public RequestMatcherAuthorizationManagerBuilder() {
        this(new NullRoleHierarchy());
    }

    private RequestMatcherAuthorizationManagerBuilder(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }


    public RequestMatcherAuthorizationManagerBuilder authenticated(RequestMatcher... requestMatchers) {
        for (RequestMatcher requestMatcher : requestMatchers) {
            mappings.add(new RequestMatcherEntry<>(requestMatcher, new AuthenticatedAuthorizationManager()));
        }
        return this;
    }

    public RequestMatcherAuthorizationManagerBuilder permitAll(RequestMatcher... requestMatchers) {
        for (RequestMatcher requestMatcher : requestMatchers) {
            mappings.add(new RequestMatcherEntry<>(requestMatcher, new PermitAllAuthorizationManager()));
        }
        return this;
    }

    public RequestMatcherAuthorizationManagerBuilder hasAuthority(String role, MvcRequestMatcher... requestMatchers) {
        for (RequestMatcher requestMatcher : requestMatchers) {
            mappings.add(new RequestMatcherEntry<>(requestMatcher, AuthorityAuthorizationManager.hasRole(role, roleHierarchy)));
        }
        return this;
    }

    public RequestMatcherAuthorizationManagerBuilder denyAll(MvcRequestMatcher... requestMatchers) {
        for (RequestMatcher requestMatcher : requestMatchers) {
            mappings.add(new RequestMatcherEntry<>(requestMatcher, new DenyAllAuthorizationManager()));
        }
        return this;
    }

    public RequestMatcherDelegatingAuthorizationManager build() {
        return new RequestMatcherDelegatingAuthorizationManager(mappings);
    }

    public static RequestMatcherAuthorizationManagerBuilder withRoleHierarchy(RoleHierarchy roleHierarchy) {
        return new RequestMatcherAuthorizationManagerBuilder(roleHierarchy);
    }

}
