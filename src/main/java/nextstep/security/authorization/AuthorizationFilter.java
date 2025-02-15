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
import java.util.Set;

public class AuthorizationFilter extends OncePerRequestFilter {

    private static final List<String> PUBLIC_URIS = List.of("/login", "/search");
    private static final List<String> AUTHENTICATION_REQUIRED_URIS = List.of("/members", "/members/me"); //

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return PUBLIC_URIS.contains(request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (!isAuthenticated()) { // 인증이 필요한데, 인증되지 않은 경우 401 응답
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (request.getRequestURI().equals("/members/me")) { // 인증된 사용자 허용
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getRequestURI().equals("/members")) { // ADMIN만 허용
            if (getAuthorities().contains("ADMIN")) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    private Set<String> getAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return Set.of();
        }

        return authentication.getAuthorities();
    }
}
