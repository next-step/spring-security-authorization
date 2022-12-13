package nextstep.security.authorization;

import nextstep.security.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            sendError(response, HttpStatus.UNAUTHORIZED);
            return;
        }

        if (!authentication.isAdmin()) {
            sendError(response, HttpStatus.FORBIDDEN);
            return;
        }

        chain.doFilter(request, response);
    }

    private static void sendError(final ServletResponse response, final HttpStatus httpStatus) throws IOException {
        ((HttpServletResponse) response).sendError(httpStatus.value(), httpStatus.getReasonPhrase());
    }
}
