package nextstep.app.config;

import nextstep.security.access.matcher.AnyRequestMatcher;
import nextstep.security.access.matcher.MvcRequestMatcher;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.BasicAuthenticationFilter;
import nextstep.security.authentication.UsernamePasswordAuthenticationFilter;
import nextstep.security.authentication.UsernamePasswordAuthenticationProvider;
import nextstep.security.authorization.AuthorizationFilter;
import nextstep.security.config.AuthorizeRequestMatcherRegistry;
import nextstep.security.config.DefaultSecurityFilterChain;
import nextstep.security.config.FilterChainProxy;
import nextstep.security.config.SecurityFilterChain;
import nextstep.security.context.HttpSessionSecurityContextRepository;
import nextstep.security.context.SecurityContextHolderFilter;
import nextstep.security.context.SecurityContextRepository;
import nextstep.security.userdetails.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

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
    public FilterChainProxy filterChainProxy() {
        return new FilterChainProxy(List.of(securityFilterChain()));
    }

    @Bean
    public SecurityFilterChain securityFilterChain() {
        List<Filter> filters = List.of(
            new SecurityContextHolderFilter(securityContextRepository()),
            new UsernamePasswordAuthenticationFilter(authenticationManager(), securityContextRepository()),
            new BasicAuthenticationFilter(authenticationManager()),
            new AuthorizationFilter()
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

    @Bean
    public AuthorizeRequestMatcherRegistry authorizeRequestMatcherRegistry() {
        return new AuthorizeRequestMatcherRegistry()
                .matcher(new MvcRequestMatcher(HttpMethod.GET, "/members")).hasAuthority("ADMIN")
                .matcher(new MvcRequestMatcher(HttpMethod.GET, "/members/me")).authenticated();
    }
}
