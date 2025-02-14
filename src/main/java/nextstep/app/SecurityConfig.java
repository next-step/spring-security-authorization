package nextstep.app;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.access.RoleHierarchy;
import nextstep.security.access.RoleHierarchyImpl;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.authentication.BasicAuthenticationFilter;
import nextstep.security.authentication.UsernamePasswordAuthenticationFilter;
import nextstep.security.authorization.AuthorizationFilter;
import nextstep.security.authorization.AuthorizationManager;
import nextstep.security.authorization.SecuredMethodInterceptor;
import nextstep.security.authorization.method.SecuredAuthorizationManager;
import nextstep.security.authorization.web.AuthenticatedAuthorizationManager;
import nextstep.security.authorization.web.AuthorityAuthorizationManager;
import nextstep.security.authorization.web.DenyAllAuthorizationManager;
import nextstep.security.authorization.web.RequestMatcherDelegatingAuthorizationManager;
import nextstep.security.config.DefaultSecurityFilterChain;
import nextstep.security.config.DelegatingFilterProxy;
import nextstep.security.config.FilterChainProxy;
import nextstep.security.config.SecurityFilterChain;
import nextstep.security.context.SecurityContextHolderFilter;
import nextstep.security.core.GrantedAuthority;
import nextstep.security.core.SimpleGrantedAuthority;
import nextstep.security.userdetails.UserDetails;
import nextstep.security.userdetails.UserDetailsService;
import nextstep.security.util.AnyRequestMatcher;
import nextstep.security.util.MvcRequestMatcher;
import nextstep.security.util.RequestMatcherEntry;
import nextstep.security.authorization.web.PermitAllAuthorizationManager;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.List;
import java.util.Set;
import org.springframework.http.HttpMethod;

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
        return new SecuredMethodInterceptor(roleHierarchy());
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.with()
                .role("ADMIN").implies("USER")
                .role("USER").implies("GUEST")
                .build();
    }

    @Bean
    public AuthorizationManager<HttpServletRequest> requestAuthorizationManager() {
        List<RequestMatcherEntry<AuthorizationManager>> mappings = new ArrayList<>();
        mappings.add(new RequestMatcherEntry<>(new MvcRequestMatcher(HttpMethod.GET, "/members"),
                new AuthorityAuthorizationManager(roleHierarchy(), Set.of("ADMIN"))));
        mappings.add(new RequestMatcherEntry<>(new MvcRequestMatcher(HttpMethod.GET, "/members/me"),
                new AuthenticatedAuthorizationManager()));
        mappings.add(new RequestMatcherEntry<>(new MvcRequestMatcher(HttpMethod.GET, "/search"),
                new PermitAllAuthorizationManager()));
        mappings.add(new RequestMatcherEntry<>(AnyRequestMatcher.INSTANCE, new DenyAllAuthorizationManager()));
        return new RequestMatcherDelegatingAuthorizationManager(mappings);
    }

    @Bean
    public SecurityFilterChain securityFilterChain() {
        return new DefaultSecurityFilterChain(
                List.of(
                        new SecurityContextHolderFilter(),
                        new UsernamePasswordAuthenticationFilter(userDetailsService()),
                        new BasicAuthenticationFilter(userDetailsService()),
                        new AuthorizationFilter(requestAuthorizationManager())
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
                    return member.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
                }
            };
        };
    }
}
