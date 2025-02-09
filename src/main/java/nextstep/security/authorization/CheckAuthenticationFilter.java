package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CheckAuthenticationFilter extends OncePerRequestFilter {
    private static final String DEFAULT_REQUEST_URI = "/members";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (List.of("/search", "/login").contains(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        if ("/members/me".equals(requestURI) && authentication.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        if (DEFAULT_REQUEST_URI.equals(requestURI) && authentication.getAuthorities().contains("ADMIN")) {
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
