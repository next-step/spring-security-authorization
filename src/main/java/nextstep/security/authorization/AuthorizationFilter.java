package nextstep.security.authorization;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.context.SecurityContextRepository;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.exception.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

public class AuthorizationFilter extends GenericFilterBean {

    private final SecurityContextRepository securityContextRepository;
    private final RoleManager roleManager;

    public AuthorizationFilter(SecurityContextRepository securityContextRepository, RoleManager roleManager) {
        this.securityContextRepository = securityContextRepository;
        this.roleManager = roleManager;
    }

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain
    ) throws IOException, ServletException {
        preAuthorization((HttpServletRequest) request);
        try {
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                throw new AuthenticationException();
            }

            if (authentication.getAuthorities().stream().noneMatch(roleManager::hasRole)) {
                throw new AuthorizationException();
            }
        } catch (AuthenticationException e) {
            ((HttpServletResponse) response).sendError(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase()
            );
            return;
        } catch (AuthorizationException e) {
            ((HttpServletResponse) response).sendError(
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase()
            );
            return;
        }

        chain.doFilter(request, response);
    }

    private void preAuthorization(HttpServletRequest request) {
        final SecurityContext context = (SecurityContext) request.getSession().getAttribute(
            SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY
        );

        final Authentication authentication = Optional.ofNullable(context)
            .map(SecurityContext::getAuthentication)
            .orElseGet(() -> securityContextRepository.loadContext(request).getAuthentication());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
