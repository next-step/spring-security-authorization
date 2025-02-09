package nextstep.security.authorization;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.authorization.manager.RequestMatcherDelegatingAuthorizationManager;
import nextstep.security.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthorizationFilter extends OncePerRequestFilter {

    private final RequestMatcherDelegatingAuthorizationManager authorizationManager;

    public AuthorizationFilter(RequestMatcherDelegatingAuthorizationManager authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthorizationDecision authorizationDecision = authorizationManager.checkInFilter(request, authentication);
        if (!authorizationDecision.isAuthorized()) {
            throw new AuthenticationException();
        }

        filterChain.doFilter(request, response);
    }
}
