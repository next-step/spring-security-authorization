package nextstep.app.config;

import java.util.List;
import nextstep.security.access.matcher.MvcRequestMatcher;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.BasicAuthenticationFilter;
import nextstep.security.authentication.UsernamePasswordAuthenticationFilter;
import nextstep.security.authentication.UsernamePasswordAuthenticationProvider;
import nextstep.security.authorization.AuthorizationFilter;
import nextstep.security.config.AuthorizationSecurityFilterChain;
import nextstep.security.config.AuthorizeRequestMatcherRegistry;
import nextstep.security.config.FilterChainProxy;
import nextstep.security.config.SecurityFilterChain;
import nextstep.security.context.HttpSessionSecurityContextRepository;
import nextstep.security.context.SecurityContextRepository;
import nextstep.security.userdetails.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    private final UserDetailsService userDetailsService;

    public AuthConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public DelegatingFilterProxy securityFilterChainProxy() {
        return new DelegatingFilterProxy("filterChainProxy");
    }

    @Bean
    public FilterChainProxy filterChainProxy(SecurityFilterChain securityFilterChain) {
        return new FilterChainProxy(securityFilterChain);
    }
    @Bean
    public SecurityFilterChain securityFilterChain(
        AuthenticationManager authenticationManager,
        SecurityContextRepository securityContextRepository
    ) {
        return new AuthorizationSecurityFilterChain(
            new UsernamePasswordAuthenticationFilter(
                authenticationManager,
                securityContextRepository
            ),
            new BasicAuthenticationFilter(
                authenticationManager,
                securityContextRepository
            ),
            new AuthorizationFilter(
                securityContextRepository,
                new AuthorizeRequestMatcherRegistry()
                    .matcher(new MvcRequestMatcher(HttpMethod.GET, "/members")).hasAuthority("ADMIN")
                    .matcher(new MvcRequestMatcher(HttpMethod.GET, "/members/me")).authenticated()
            )
        );
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new AuthenticationManager(new UsernamePasswordAuthenticationProvider(userDetailsService));
    }

}
