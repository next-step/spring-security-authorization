package nextstep.security.authorization;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Collection;

public class AuthorizationFilter extends GenericFilterBean {
    private final AuthorizationManager<RequestAuthorizationContext<Collection<GrantedAuthority>>> authorizationManager;

    public AuthorizationFilter(AuthorizationManager<RequestAuthorizationContext<Collection<GrantedAuthority>>> authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var httpServletRequest = (HttpServletRequest) servletRequest;
        var httpServletResponse = (HttpServletResponse) servletResponse;
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        var authorizationDecision = authorizationManager.check(authentication, new RequestAuthorizationAuthoritiesContext(httpServletRequest, authentication.getAuthorities()));
        if (!authorizationDecision.isSuccess()) {
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
