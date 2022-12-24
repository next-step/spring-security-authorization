package nextstep.app.config;

import java.util.List;
import javax.servlet.Filter;
import nextstep.security.access.matcher.AnyRequestMatcher;
import nextstep.security.access.matcher.MvcRequestMatcher;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.BasicAuthenticationFilter;
import nextstep.security.authentication.UsernamePasswordAuthenticationFilter;
import nextstep.security.authentication.UsernamePasswordAuthenticationProvider;
import nextstep.security.authorization.AuthorizationFilter;
import nextstep.security.authorization.PreAuthorizationFilter;
import nextstep.security.config.AuthorizeRequestMatcherRegistry;
import nextstep.security.config.DefaultSecurityFilterChain;
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
        final List<Filter> filters = List.of(
            new UsernamePasswordAuthenticationFilter(
                authenticationManager,
                securityContextRepository
            ),
            new BasicAuthenticationFilter(
                authenticationManager,
                securityContextRepository
            ),
            new PreAuthorizationFilter(securityContextRepository),
            new AuthorizationFilter(
                new AuthorizeRequestMatcherRegistry()
                    .matcher(new MvcRequestMatcher(HttpMethod.GET, "/members")).hasAuthority("ADMIN")
                    .matcher(new MvcRequestMatcher(HttpMethod.GET, "/members/me")).authenticated()
            )
        );
        return new DefaultSecurityFilterChain(AnyRequestMatcher.INSTANCE, filters);
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new AuthenticationManager(new UsernamePasswordAuthenticationProvider(userDetailsService));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver());
    }
}
