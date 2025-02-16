package nextstep.app;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.domain.Role;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.authentication.BasicAuthenticationFilter;
import nextstep.security.authentication.UsernamePasswordAuthenticationFilter;
import nextstep.security.authorization.*;
import nextstep.security.config.DefaultSecurityFilterChain;
import nextstep.security.config.DelegatingFilterProxy;
import nextstep.security.config.FilterChainProxy;
import nextstep.security.config.SecurityFilterChain;
import nextstep.security.context.SecurityContextHolderFilter;
import nextstep.security.requestmatcher.AnyRequestMatcher;
import nextstep.security.requestmatcher.MvcRequestMatcher;
import nextstep.security.requestmatcher.RequestMatcherEntry;
import nextstep.security.userdetails.UserDetails;
import nextstep.security.userdetails.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpMethod;

import java.util.Collection;
import java.util.List;
import java.util.Set;

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
        return new DefaultSecurityFilterChain(
                List.of(
                        new SecurityContextHolderFilter(),
                        new UsernamePasswordAuthenticationFilter(userDetailsService()),
                        new BasicAuthenticationFilter(userDetailsService()),
                        new AuthorizationFilter(requestMatcherDelegatingAuthorizationManager())
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
                public Set<GrantedAuthority> getAuthorities() {
                    return member.getRoles();
                }
            };
        };
    }

    @Bean
    public RequestMatcherDelegatingAuthorizationManager requestMatcherDelegatingAuthorizationManager() {
        List<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext<Collection<GrantedAuthority>>>>> mappings = List.of(
                new RequestMatcherEntry<>(MvcRequestMatcher.of(HttpMethod.GET, "/members/me"), new AuthenticatedAuthorizationManager<>()),
                new RequestMatcherEntry<>(MvcRequestMatcher.of(HttpMethod.GET, "/members"), new AuthoritiesAuthorizationManager()),
                new RequestMatcherEntry<>(MvcRequestMatcher.of(HttpMethod.GET, "/search"), new PermitAllAuthorizationManager<>()),
                new RequestMatcherEntry<>(AnyRequestMatcher.INSTANCE, new PermitAllAuthorizationManager<>())
        );

        return new RequestMatcherDelegatingAuthorizationManager(mappings);
    }
}
