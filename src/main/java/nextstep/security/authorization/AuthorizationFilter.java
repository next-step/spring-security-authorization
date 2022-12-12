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
import nextstep.security.config.AuthorizeRequestMatcherRegistry;
import nextstep.security.config.AuthorizeRequestMatcherRegistry.AuthorizedUrl;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.context.SecurityContextRepository;
import nextstep.security.exception.AccessDeniedException;
import nextstep.security.exception.AuthenticationException;
import nextstep.security.exception.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

public class AuthorizationFilter extends GenericFilterBean {

    private final SecurityContextRepository securityContextRepository;
    private final AuthorizeRequestMatcherRegistry authorizeRequestMatcherRegistry;

    public AuthorizationFilter(
        SecurityContextRepository securityContextRepository,
        AuthorizeRequestMatcherRegistry authorizeRequestMatcherRegistry
    ) {
        this.securityContextRepository = securityContextRepository;
        this.authorizeRequestMatcherRegistry = authorizeRequestMatcherRegistry;
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

            final String attribute = authorizeRequestMatcherRegistry.getAttribute((HttpServletRequest) request);
            if (attribute == null || AuthorizedUrl.PERMIT_ALL.equals(attribute)) {
                chain.doFilter(request, response);
                return;
            } else if (AuthorizedUrl.DENY_ALL.equals(attribute)) {
                throw new AccessDeniedException();
            } else if (AuthorizedUrl.AUTHENTICATED.equals(attribute)) {
                if (authentication == null) {
                    throw new AuthenticationException();
                }
            } else if (authentication.getAuthorities().stream().noneMatch(it -> attribute.contains(it))) {
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
        } catch (AccessDeniedException e) {
            ((HttpServletResponse) response).sendError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase()
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
