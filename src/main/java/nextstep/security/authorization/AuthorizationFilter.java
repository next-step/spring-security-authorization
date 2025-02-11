package nextstep.security.authorization;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;


public class AuthorizationFilter extends OncePerRequestFilter {
    private static final String DEFAULT_REQUEST_URI = "/members";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> authorities = authentication.getAuthorities();

        if (DEFAULT_REQUEST_URI.equals(request.getRequestURI())) {
            if (authorities.contains("ADMIN")) {
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        if ("/members/me".equals(request.getRequestURI())) {
            if (authentication.isAuthenticated()) {
                request.setAttribute("username", authentication.getPrincipal());
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
