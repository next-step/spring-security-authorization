package nextstep.security.authentication;

import nextstep.security.access.matcher.MvcRequestMatcher;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.context.SecurityContextRepository;
import nextstep.security.exception.AuthenticationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UsernamePasswordAuthenticationFilter extends GenericFilterBean {

    private static final MvcRequestMatcher DEFAULT_REQUEST_MATCHER = new MvcRequestMatcher(HttpMethod.POST, "/login");

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public UsernamePasswordAuthenticationFilter(final AuthenticationManager authenticationManager,
                                                final SecurityContextRepository securityContextRepository) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        final var request = (HttpServletRequest) servletRequest;

        try {
            if (!DEFAULT_REQUEST_MATCHER.matches(request)) {
                chain.doFilter(request, response);
                return;
            }

            String username = request.getParameter("username");
            String password = request.getParameter("password");

            final var authRequest = UsernamePasswordAuthentication.ofRequest(username,
                    password);
            final var authResult = authenticationManager.authenticate(authRequest);

            final var context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authResult);
            SecurityContextHolder.setContext(context);

            securityContextRepository.saveContext(context, request, (HttpServletResponse) response);
        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
            ((HttpServletResponse) response).sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return;
        }

        chain.doFilter(request, response);
    }
}
