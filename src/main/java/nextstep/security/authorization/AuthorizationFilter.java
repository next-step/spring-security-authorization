package nextstep.security.authorization;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.context.SecurityContextHolder;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.Set;

public class AuthorizationFilter extends OncePerRequestFilter {

    private static final String ADMIN_ROLE_CHECK_URI = "/members";
    private static final String AUTH_CHECK_URI = "/members/me";
    private static final String[] PERMIT_ALL_URI = new String[]{"/login", "/search"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        boolean isNotGetMethod = !HttpMethod.GET.name().equalsIgnoreCase(method);
        boolean needRoleCheck = ADMIN_ROLE_CHECK_URI.equals(requestURI);
        boolean needLoginCheck = AUTH_CHECK_URI.equals(requestURI);
        boolean isPermitAll = Arrays.asList(PERMIT_ALL_URI).contains(requestURI);

        if (isNotGetMethod ||
                (!needRoleCheck && !isPermitAll && !needLoginCheck)) {
            throw new AccessDeniedException("Not allowed to get members from " + requestURI);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (needRoleCheck) {
            // Should be Annotation Check
            if (authentication == null) {
                throw new AuthenticationException();
            }
            Set<String> authorities = authentication.getAuthorities();
            if (authorities.stream().noneMatch("ADMIN"::equalsIgnoreCase)) {
                throw new ForbiddenException();
            }
        } else if (needLoginCheck && (authentication == null || !authentication.isAuthenticated())) {
            // Should isAuthenticated Check
            throw new AuthenticationException();
        }

        filterChain.doFilter(request, response);
    }
}
