package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CheckAuthenticationFilter extends OncePerRequestFilter {
    private final AuthorizationManager<HttpServletRequest> authorizationManager;

    public CheckAuthenticationFilter(AuthorizationManager<HttpServletRequest> authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        AuthorizationDecision decision = authorizationManager.check(authentication, request);

        if (decision.result) {
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}
