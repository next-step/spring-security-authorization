package nextstep.app;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.authentication.BasicAuthenticationFilter;
import nextstep.security.authentication.UsernamePasswordAuthenticationFilter;
import nextstep.security.authorization.AuthorizationFilter;
import nextstep.security.authorization.SecuredMethodInterceptor;
import nextstep.security.authorization.manager.AuthenticatedAuthorizationManager;
import nextstep.security.authorization.manager.AuthorityAuthorizationManager;
import nextstep.security.authorization.manager.AuthorizationManager;
import nextstep.security.authorization.manager.DenyAllAuthorizationManager;
import nextstep.security.authorization.manager.PermitAllAuthorizationManager;
import nextstep.security.authorization.manager.RequestAuthorizationManager;
import nextstep.security.config.DefaultSecurityFilterChain;
import nextstep.security.config.DelegatingFilterProxy;
import nextstep.security.config.FilterChainProxy;
import nextstep.security.config.SecurityFilterChain;
import nextstep.security.context.SecurityContextHolderFilter;
import nextstep.security.userdetails.UserDetails;
import nextstep.security.userdetails.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.List;
import java.util.Set;

import static nextstep.security.authorization.matcher.RequestMatcherEntry.createDefaultMatcher;
import static nextstep.security.authorization.matcher.RequestMatcherEntry.createMvcMatcher;
import static org.springframework.http.HttpMethod.GET;

@EnableAspectJAutoProxy
@Configuration
public class SecurityConfig {

    private final MemberRepository memberRepository;

    public SecurityConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public DelegatingFilterProxy delegatingFilterProxy() {
        return new DelegatingFilterProxy(filterChainProxy(List.of(securityFilterChain())));
    }

    @Bean
    public FilterChainProxy filterChainProxy(List<SecurityFilterChain> securityFilterChains) {
        return new FilterChainProxy(securityFilterChains);
    }

    @Bean
    public SecuredMethodInterceptor securedMethodInterceptor() {
        return new SecuredMethodInterceptor();
    }

    @Bean
    public SecurityFilterChain securityFilterChain() {
        final AuthorizationManager<HttpServletRequest> authorizationManager = new RequestAuthorizationManager(List.of(
                createMvcMatcher(GET, "/members", new AuthorityAuthorizationManager<>("ADMIN")),
                createMvcMatcher(GET, "/members/me", new AuthenticatedAuthorizationManager<>()),
                createMvcMatcher(GET, "/search", new PermitAllAuthorizationManager<>())
        ), createDefaultMatcher(new DenyAllAuthorizationManager<>()));
        return new DefaultSecurityFilterChain(
                List.of(
                        new SecurityContextHolderFilter(),
                        new UsernamePasswordAuthenticationFilter(userDetailsService()),
                        new BasicAuthenticationFilter(userDetailsService()),
                        new AuthorizationFilter(authorizationManager)
                )
        );
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Member member = memberRepository.findByEmail(username)
                    .orElseThrow(() -> new AuthenticationException("존재하지 않는 사용자입니다."));

            return new UserDetails() {
                @Override
                public String getUsername() {
                    return member.getEmail();
                }

                @Override
                public String getPassword() {
                    return member.getPassword();
                }

                @Override
                public Set<String> getAuthorities() {
                    return member.getRoles();
                }
            };
        };
    }
}
