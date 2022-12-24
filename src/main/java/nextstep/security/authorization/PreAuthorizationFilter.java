package nextstep.security.authorization;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
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
        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        final Optional<SecurityContext> securityContext = Optional.ofNullable(
            securityContextRepository.loadContext(httpServletRequest)
        );
        securityContext.ifPresent(it -> SecurityContextHolder.setContext(it));

        chain.doFilter(request, response);
    }
}
