package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.config.AuthorizeRequestMatcherRegistry;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.exception.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RoleAuthorizationFilter extends GenericFilterBean {
    private static final String ADMIN_ROLE = "ADMIN";

    private final AuthorizeRequestMatcherRegistry requestMatcherRegistry;

    public RoleAuthorizationFilter(AuthorizeRequestMatcherRegistry requestMatcherRegistry) {
        this.requestMatcherRegistry = requestMatcherRegistry;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String attribute = requestMatcherRegistry.getAttribute((HttpServletRequest) request);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boolean isAuthorized = requestMatcherRegistry.isAuthorized(attribute, authentication);

            if (!isAuthorized) {
                ((HttpServletResponse) response).sendError(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase());
                return;
            }

        } catch (AuthenticationException e) {
            ((HttpServletResponse) response).sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return;
        }

        chain.doFilter(request, response);
    }

}
