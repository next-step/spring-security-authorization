package nextstep.security.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import nextstep.security.fixture.MockFilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;

class FilterChainProxyTest {

    private FilterChainProxy filterChainProxy;
    private TestFilter loginTestFilter;
    private TestFilter membersTestFilter;

    @BeforeEach
    void setUp() {
        loginTestFilter = new TestFilter();
        membersTestFilter = new TestFilter();

        filterChainProxy = new FilterChainProxy(
            new DefaultSecurityFilterChain(
                loginTestFilter,
                membersTestFilter
            )
        );
    }

    @Test
    void login() throws ServletException, IOException {
        HttpServletRequest request = new MockHttpServletRequest(HttpMethod.POST.name(), "/login");
        filterChainProxy.doFilter(request, null, new MockFilterChain());

        assertAll(
            () -> assertThat(loginTestFilter.count).isEqualTo(1),
            () -> assertThat(membersTestFilter.count).isEqualTo(1)
        );
    }

    @Test
    void members() throws ServletException, IOException {
        HttpServletRequest request = new MockHttpServletRequest(HttpMethod.GET.name(), "/members");
        filterChainProxy.doFilter(request, null, new MockFilterChain());

        assertAll(
            () -> assertThat(loginTestFilter.count).isEqualTo(1),
            () -> assertThat(membersTestFilter.count).isEqualTo(1)
        );
    }

    @Test
    void none() throws ServletException, IOException {
        HttpServletRequest request = new MockHttpServletRequest(HttpMethod.GET.name(), "/test");
        filterChainProxy.doFilter(request, null, new MockFilterChain());

        assertAll(
            () -> assertThat(loginTestFilter.count).isEqualTo(1),
            () -> assertThat(membersTestFilter.count).isEqualTo(1)
        );
    }

    private static class TestFilter implements Filter {

        private int count = 0;

        @Override
        public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
        ) throws ServletException, IOException {
            count++;
            chain.doFilter(request, response);
        }
    }
}
