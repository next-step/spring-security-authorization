package nextstep.security.authorization;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class CheckAuthenticationFilter extends GenericFilterBean {

    private final AuthorizationManager<HttpServletRequest> authorizationManager;
    private final AuthorizationDeniedHandler authorizationDeniedHandler;

    public CheckAuthenticationFilter(AuthorizationManager<HttpServletRequest> authorizationManager) {
        this(authorizationManager, response -> response.setStatus(HttpServletResponse.SC_FORBIDDEN));
    }

    public CheckAuthenticationFilter(AuthorizationManager<HttpServletRequest> authorizationManager
            , AuthorizationDeniedHandler authorizationDeniedHandler
    ) {
        this.authorizationManager = authorizationManager;
        this.authorizationDeniedHandler = authorizationDeniedHandler;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        AuthorizationDecision authorizationDecision = authorizationManager.check(authentication, (HttpServletRequest) request);

        if (authorizationDecision.isDeny()) {
            authorizationDeniedHandler.handle((HttpServletResponse) response);
            return;
        }

        chain.doFilter(request, response);
    }
}
