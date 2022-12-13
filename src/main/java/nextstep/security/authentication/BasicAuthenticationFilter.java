package nextstep.security.authentication;

import nextstep.security.context.SecurityContextHolder;
import nextstep.security.exception.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

public class BasicAuthenticationFilter extends GenericFilterBean {

    private final AuthenticationManager authenticationManager;

    public BasicAuthenticationFilter(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {
            final var authRequest = convertAuthenticationRequest((HttpServletRequest) request);

            if (authRequest == null) {
                chain.doFilter(request, response);
                return;
            }

            final var authResult = authenticationManager.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(authResult);
        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
            ((HttpServletResponse) response).sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return;
        }

        chain.doFilter(request, response);
    }

    private Authentication convertAuthenticationRequest(HttpServletRequest request) {
        Optional<String> header = Optional.ofNullable(request.getHeader("Authorization"));

        if (header.isEmpty() || !header.get().startsWith("Basic ")) {
            return null;
        }

        byte[] base64Token = header.get().substring(6).getBytes(StandardCharsets.UTF_8);
        byte[] decoded = Base64.getDecoder().decode(base64Token);
        String token = new String(decoded);

        int delim = token.indexOf(":");
        if (delim == -1) {
            return null;
        }

        String email = token.substring(0, delim);
        String password = token.substring(delim + 1);

        return UsernamePasswordAuthentication.ofRequest(email, password);
    }
}
