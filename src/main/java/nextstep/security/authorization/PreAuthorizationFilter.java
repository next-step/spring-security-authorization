package nextstep.security.authorization;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.context.SecurityContextRepository;
import org.springframework.web.filter.GenericFilterBean;

public class PreAuthorizationFilter extends GenericFilterBean {

    private final SecurityContextRepository securityContextRepository;

    public PreAuthorizationFilter(SecurityContextRepository securityContextRepository) {
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain
    ) throws IOException, ServletException {
        try {
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                chain.doFilter(request, response);
                return;
            }

            final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            SecurityContext context = Optional.ofNullable(
                    (SecurityContext) httpServletRequest.getSession()
                        .getAttribute(SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY)
                )
                .orElseGet(() -> securityContextRepository.loadContext(httpServletRequest));

            final Authentication authentication = Optional.ofNullable(context)
                .map(it -> it.getAuthentication())
                .orElse(null);

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception ignored) {

        }
        chain.doFilter(request, response);
    }
}
