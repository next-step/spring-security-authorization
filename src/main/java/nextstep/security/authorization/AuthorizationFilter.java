package nextstep.security.authorization;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

public class AuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (request.getRequestURI().equals("/members")) {
            if (!isAuthenticated(response, authentication)) {
                throw new AuthenticationException();
            }

            if (!isGranted(authentication, "ADMIN")) {
                throw new ForbiddenException();
            }
        }

        if (request.getRequestURI().equals("/members/me")) {
            if (!isAuthenticated(response, authentication)) {
                throw new AuthenticationException();
            }
        }

        if (request.getRequestURI().equals("/search")) {
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isGranted(final Authentication authentication, final String authoritiesURI) {
        final Set<String> authorities = authentication.getAuthorities();
        return authorities.contains(authoritiesURI);
    }

    private boolean isAuthenticated(final HttpServletResponse response, final Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return true;
    }
}
