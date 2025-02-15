package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletResponse;


@FunctionalInterface
public interface AuthorizationDeniedHandler {
    void handle(HttpServletResponse response);
}
