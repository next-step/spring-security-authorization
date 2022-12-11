package nextstep.security.authorization;

import nextstep.security.config.AuthorizeRequestMatcherRegistry;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.context.SecurityContextRepository;
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

public class SessionAuthorizationFilter extends GenericFilterBean {

    private final SecurityContextRepository securityContextRepository;

    private final AuthorizeRequestMatcherRegistry requestMatcherRegistry;

    public SessionAuthorizationFilter(SecurityContextRepository securityContextRepository, AuthorizeRequestMatcherRegistry requestMatcherRegistry) {
        this.securityContextRepository = securityContextRepository;
        this.requestMatcherRegistry = requestMatcherRegistry;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String attribute = requestMatcherRegistry.getAttribute((HttpServletRequest) request);
            SecurityContext loadedContext = securityContextRepository.loadContext((HttpServletRequest) request);
            if (loadedContext == null) {
                ((HttpServletResponse) response).sendError(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase());
                return;
            }
            boolean isAuthorized = requestMatcherRegistry.isAuthorized(attribute, loadedContext.getAuthentication());

            if (isAuthorized) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(loadedContext.getAuthentication());
                SecurityContextHolder.setContext(context);
            }
        } catch (AuthenticationException e) {
            ((HttpServletResponse) response).sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return;
        }

        chain.doFilter(request, response);
    }

}
