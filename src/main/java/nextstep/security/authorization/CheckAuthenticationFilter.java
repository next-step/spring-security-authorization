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

public class CheckAuthenticationFilter extends OncePerRequestFilter {
    private final RequestMatcherDelegatingAuthorizationManager requestAuthorizationManager;

    public CheckAuthenticationFilter(RequestMatcherDelegatingAuthorizationManager requestAuthorizationManager) {
        this.requestAuthorizationManager = requestAuthorizationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            requestAuthorizationManager.check(authentication, request);
        } catch (AuthenticationException ae) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (ForbiddenException fe) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
