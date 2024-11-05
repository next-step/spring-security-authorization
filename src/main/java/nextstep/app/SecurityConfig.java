package nextstep.app;

import java.util.List;
import java.util.Set;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.SimpleLogFilter;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.authentication.BasicAuthenticationFilter;
import nextstep.security.authentication.UsernamePasswordAuthenticationFilter;
import nextstep.security.authorization.AuthorizationDecision;
import nextstep.security.authorization.AuthorizationFilter;
import nextstep.security.authorization.manager.AuthenticatedAuthorizationManager;
import nextstep.security.authorization.manager.AuthorityAuthorizationManager;
import nextstep.security.authorization.manager.AuthorizationManager;
import nextstep.security.authorization.manager.RequestMatcherDelegatingAuthorizationManager;
import nextstep.security.authorization.matcher.AnyRequestMatcher;
import nextstep.security.authorization.matcher.MvcRequestMatcher;
import nextstep.security.authorization.matcher.RequestMatcherEntry;
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
    public RequestMatcherDelegatingAuthorizationManager requestMatcherDelegatingAuthorizationManager() {
        AuthorizationManager permitAllAuthorizationManager = new AuthorizationManager() {
            @Override
            public AuthorizationDecision check(Authentication authentication, Object object) {
                // 다 통과해!
                return new AuthorizationDecision(true);
            }
        };

        // member/me 는 인증 정보만 있으면 통과
        RequestMatcherEntry membersMe = new RequestMatcherEntry(
                new MvcRequestMatcher(HttpMethod.GET, "/members/me"),
                new AuthenticatedAuthorizationManager());

        // members 는 관리자 권한을 가진 인증 정보가 있으면 통과
        RequestMatcherEntry members = new RequestMatcherEntry(
                new MvcRequestMatcher(HttpMethod.GET, "/members"),
                new AuthorityAuthorizationManager("ADMIN"));

        // search 는 통과
        RequestMatcherEntry search = new RequestMatcherEntry(
                new MvcRequestMatcher(HttpMethod.GET, "/search"), permitAllAuthorizationManager);

        // 나머지 모든 API 는 통과
        RequestMatcherEntry any = new RequestMatcherEntry(new AnyRequestMatcher(),
                permitAllAuthorizationManager);

        return new RequestMatcherDelegatingAuthorizationManager(
                List.of(membersMe, members, search, any));
    }

    @Bean
    public SecurityFilterChain securityFilterChain() {
        return new DefaultSecurityFilterChain(
                List.of(
                        new SecurityContextHolderFilter(),
                        new UsernamePasswordAuthenticationFilter(userDetailsService()),
                        new BasicAuthenticationFilter(userDetailsService()),
                        new AuthorizationFilter(requestMatcherDelegatingAuthorizationManager()),
                        new SimpleLogFilter()
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
