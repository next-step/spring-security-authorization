package nextstep.security.context;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class SecurityContextHolderFilter extends OncePerRequestFilter {

    private final SecurityContextRepository securityContextRepository;

    public SecurityContextHolderFilter(final SecurityContextRepository securityContextRepository) {
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        Optional.ofNullable(securityContextRepository.loadContext(request))
                .ifPresent(SecurityContextHolder::setContext);

        filterChain.doFilter(request, response);
    }
}
