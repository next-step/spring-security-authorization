package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class CheckAuthenticationFilter extends OncePerRequestFilter {
    private static final String DEFAULT_REQUEST_URI = "/members";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (!DEFAULT_REQUEST_URI.equals(request.getRequestURI())) {
            request.setAttribute("username", authentication.getPrincipal());
            filterChain.doFilter(request, response);
            return;
        }

        Set<String> authorities = authentication.getAuthorities();
        if (!authorities.contains("ADMIN")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
