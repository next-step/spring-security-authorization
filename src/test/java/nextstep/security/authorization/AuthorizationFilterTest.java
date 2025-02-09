package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.authorization.manager.*;
import nextstep.security.matcher.AnyRequestMatcher;
import nextstep.security.matcher.MvcRequestMatcher;
import nextstep.security.matcher.RequestMatcherEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthorizationFilterTest {

    private final AuthorizationFilter authorizationFilter;

    AuthorizationFilterTest() {
        List<RequestMatcherEntry<AuthorizationManager>> mappings = new ArrayList<>();
        mappings.add(new RequestMatcherEntry<>(new MvcRequestMatcher(HttpMethod.GET, "/members/me"), new AuthenticatedAuthorizationManager()));
        mappings.add(new RequestMatcherEntry<>(new MvcRequestMatcher(HttpMethod.GET, "/members"), new AuthorityAuthorizationManager()));
        mappings.add(new RequestMatcherEntry<>(new MvcRequestMatcher(HttpMethod.GET, "/search", "/login"), new PermitAllAuthorizationManager()));
        mappings.add(new RequestMatcherEntry<>(new AnyRequestMatcher(), new DenyAllAuthorizationManager()));
        RequestMatcherDelegatingAuthorizationManager authorizationManager = new RequestMatcherDelegatingAuthorizationManager(mappings);
        this.authorizationFilter = new AuthorizationFilter(authorizationManager);
        ;
    }

    @DisplayName("필터에서는 로그인, 검색 API는 인증을 진행하지 않는다.")
    @ParameterizedTest
    @ValueSource(strings = {"/login", "/search"})
    void filter_does_not_check_permit_all(String url) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        request.setMethod("GET");
        request.setRequestURI(url);
        authorizationFilter.doFilterInternal(request, response, chain);

        assertThat(chain.getRequest()).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
    }

    @DisplayName("필터에서는 인증이 필요한 API는 검사한다.")
    @ParameterizedTest
    @ValueSource(strings = {"/members/me", "/blah/blah"})
    void filter_check_apis(String url) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        request.setMethod("GET");
        request.setRequestURI(url);
        assertThatThrownBy(() -> authorizationFilter.doFilterInternal(request, response, chain))
                .isInstanceOfAny(AccessDeniedException.class, AuthenticationException.class);
    }
}
