package nextstep.security.authorization;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.request.MvcRequestMatcher;
import nextstep.security.request.RequestMatcherEntry;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

public class AuthorizationFilter extends GenericFilterBean {

    private final RequestAuthorizationManager authorizationManager = new RequestAuthorizationManager(
            List.of(
                    new RequestMatcherEntry<>(new MvcRequestMatcher(HttpMethod.GET, "/members"), new AuthorityAuthorizationManager("ADMIN")),
                    new RequestMatcherEntry<>(new MvcRequestMatcher(HttpMethod.GET, "/members/me"), new AuthorityAuthorizationManager("")),
                    new RequestMatcherEntry<>(new MvcRequestMatcher(HttpMethod.GET, "/search"), AuthorityAuthorizationManager.permitAll())
            )
    );

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain) throws IOException, ServletException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final AuthorizationDecision decision = this.authorizationManager.check(authentication, (HttpServletRequest) request);

        if (decision == null || !decision.isGranted()) {
            throw new ForbiddenException();
        }

        filterChain.doFilter(request, response);
    }
}
