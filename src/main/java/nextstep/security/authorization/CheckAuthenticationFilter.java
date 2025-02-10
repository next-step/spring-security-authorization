package nextstep.security.authorization;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class CheckAuthenticationFilter extends OncePerRequestFilter {
    private static final String LOGIN_REQUEST_URI = "/login";
    private static final String PRIVATE_REQUEST_URI = "/members/me";
    private static final String ADMIN_REQUEST_URI = "/members";
    private static final String SEARCH_REQUEST_URI = "/search";

    private static final List<String> ALLOW_URI = List.of(LOGIN_REQUEST_URI, PRIVATE_REQUEST_URI, ADMIN_REQUEST_URI, SEARCH_REQUEST_URI);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestURI = request.getRequestURI();

        if (!ALLOW_URI.contains(requestURI)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (List.of(SEARCH_REQUEST_URI, LOGIN_REQUEST_URI).contains(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (PRIVATE_REQUEST_URI.equals(requestURI)) {
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
