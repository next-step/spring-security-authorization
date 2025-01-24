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

public class AuthorizationFilter extends OncePerRequestFilter {

    private final AuthorizationManager<HttpServletRequest> authorizationManager = new RequestAuthorizationManager();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AuthorizationDecision decision = authorizationManager.check(authentication, request);
            if (decision == null || !decision.isGranted()) {
                throw new ForbiddenException();
            }

        } catch (AuthenticationException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (ForbiddenException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean checkAuthorization(Authentication authentication, HttpServletRequest httpRequest) {
        if (httpRequest.getRequestURI().equals("/members")) {
            if (authentication == null) {
                throw new AuthenticationException();
            }

            return authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.equals("ADMIN"));
        }

        if (httpRequest.getRequestURI().equals("/members/me")) {
            if (authentication == null) {
                throw new AuthenticationException();
            }

            return authentication.isAuthenticated();
        }

        if (httpRequest.getRequestURI().equals("/search")) {
            return true;
        }

        return false;
    }
}
