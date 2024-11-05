package nextstep.security.authorization;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.security.authentication.Authentication;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            checkAuthorities(request, response, filterChain);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    private static void checkAuthorities(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {
        // 시큐리티 컨텍스트를 통해 인증 객체를 가져온다.
        SecurityContext securityContext = SecurityContextHolder.getContext();

        // 가져온 객체에 권한이 있는지 체크한다.
        Authentication authentication = securityContext.getAuthentication();

        // 권한이 없으민 403 을 반환한다.
        if (!authentication.getAuthorities().contains("ADMIN")) {
            throw new AuthorizationException();
        }

        // 권한이 있으면 통과한다.
        filterChain.doFilter(request, response);
    }
}
