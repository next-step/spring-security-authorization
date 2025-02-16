package nextstep.app;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authorization.Secured;
import nextstep.security.authorization.access.AnyRequestMatcher;
import nextstep.security.authorization.access.MvcRequestMatcher;
import nextstep.security.authorization.access.RequestMatcherEntry;
import nextstep.security.authorization.manager.AuthenticatedAuthorizationManager;
import nextstep.security.authorization.manager.AuthorizationManager;
import nextstep.security.authorization.manager.DenyAllAuthorizationManager;
import nextstep.security.authorization.manager.HasAuthorityAuthorizationManager;
import nextstep.security.authorization.manager.PermitAllAuthorizationManager;
import nextstep.security.authorization.manager.RequestMatcherDelegatingAuthorizationManager;
import nextstep.security.authorization.role.RoleHierarchy;
import nextstep.security.authorization.role.RoleHierarchyImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public RequestMatcherDelegatingAuthorizationManager requestAuthorizationManager() {
        List<RequestMatcherEntry<AuthorizationManager<HttpServletRequest>>> mappings = new ArrayList<>();

        mappings.add(new RequestMatcherEntry<>(
                new MvcRequestMatcher(HttpMethod.GET, "/members/me"),
                new AuthenticatedAuthorizationManager<>())
        );
        mappings.add(new RequestMatcherEntry<>(
                new MvcRequestMatcher(HttpMethod.GET, "/members"),
                new HasAuthorityAuthorizationManager<>(roleHierarchy(), "ADMIN"))
        );
        mappings.add(new RequestMatcherEntry<>(
                new MvcRequestMatcher(HttpMethod.GET, "/search"),
                new PermitAllAuthorizationManager<>())
        );

        mappings.add(new RequestMatcherEntry<>(
                new MvcRequestMatcher(HttpMethod.GET, "/user-granted"),
                new HasAuthorityAuthorizationManager<>(roleHierarchy(), "USER"))
        );

        mappings.add(new RequestMatcherEntry<>(
                new MvcRequestMatcher(HttpMethod.GET, "/admin-granted"),
                new HasAuthorityAuthorizationManager<>(roleHierarchy(), "ADMIN"))
        );

        mappings.add(new RequestMatcherEntry<>(
                new AnyRequestMatcher(),
                new DenyAllAuthorizationManager<>())
        );

        return new RequestMatcherDelegatingAuthorizationManager(mappings);
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.with()
                .role("ADMIN").implies("USER")
                .build();
    }

    @RestController
    static class TestController {
        @GetMapping("/invalid")
        public String forbidden() {
            return "forbidden";
        }

        @Secured("USER")
        @GetMapping("/user-granted")
        public String userGranted() {
            return "userGranted";
        }

        @Secured("ADMIN")
        @GetMapping("/admin-granted")
        public String adminGranted() {
            return "adminGranted";
        }
    }

}
