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
import nextstep.security.authorization.manager.RoleManager;
import nextstep.security.config.AuthorizeRequestMatcherRegistry;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.exception.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

public class AuthorizationFilter extends GenericFilterBean {

    private final AuthorizeRequestMatcherRegistry authorizeRequestMatcherRegistry;

    public AuthorizationFilter(AuthorizeRequestMatcherRegistry authorizeRequestMatcherRegistry) {
        this.authorizeRequestMatcherRegistry = authorizeRequestMatcherRegistry;
    }

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain
    ) throws IOException, ServletException {
        try {
            final Authentication authentication = Optional.ofNullable(
                SecurityContextHolder
                    .getContext()
                    .getAuthentication()
            ).orElseThrow(AuthenticationException::new);

            final RoleManager roleManager = authorizeRequestMatcherRegistry.getRoleManager((HttpServletRequest) request);

            if (roleManager == null) {
                return;
            }


            if (!roleManager.check(authentication)) {
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
}
