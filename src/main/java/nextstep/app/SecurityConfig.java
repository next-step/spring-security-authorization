package nextstep.app;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.authentication.BasicAuthenticationFilter;
import nextstep.security.authentication.UsernamePasswordAuthenticationFilter;
import nextstep.security.authorization.AuthorizationFilter;
import nextstep.security.authorization.SecuredMethodInterceptor;
import nextstep.security.authorization.manager.*;
import nextstep.security.config.DefaultSecurityFilterChain;
import nextstep.security.config.DelegatingFilterProxy;
import nextstep.security.config.FilterChainProxy;
import nextstep.security.config.SecurityFilterChain;
import nextstep.security.context.SecurityContextHolderFilter;
import nextstep.security.matcher.AnyRequestMatcher;
import nextstep.security.matcher.MvcRequestMatcher;
import nextstep.security.matcher.RequestMatcherEntry;
import nextstep.security.role.*;
import nextstep.security.userdetails.UserDetails;
import nextstep.security.userdetails.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        AuthorityAuthorizationManager<Set<GrantedAuthority>> authorityAuthorizationManager = new AuthorityAuthorizationManager<>(roleHierarchy());
        return new SecuredMethodInterceptor(new SecuredAuthorizationManager(authorityAuthorizationManager));
    }

    @Bean
    public RequestMatcherDelegatingAuthorizationManager requestMatcherDelegatingAuthorizationManager() {
        List<RequestMatcherEntry<AuthorizationManager<HttpServletRequest>>> mappings = new ArrayList<>();
        mappings.add(new RequestMatcherEntry<>(new MvcRequestMatcher(HttpMethod.GET, "/members/me"), new AuthenticatedAuthorizationManager()));
        mappings.add(new RequestMatcherEntry<>(new MvcRequestMatcher(HttpMethod.GET, "/members"), new AuthorityAuthorizationManager<>(new NullRoleHierarchy())));
        mappings.add(new RequestMatcherEntry<>(new MvcRequestMatcher(HttpMethod.GET, "/search", "/login"), new PermitAllAuthorizationManager()));
        mappings.add(new RequestMatcherEntry<>(new AnyRequestMatcher(), new DenyAllAuthorizationManager()));
        return new RequestMatcherDelegatingAuthorizationManager(mappings);
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
                    return member.getRoles()
                            .stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toSet());
                }
            };
        };
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.builder()
                .role("ADMIN").addImplies("MEMBER")
                .build();
    }
}
