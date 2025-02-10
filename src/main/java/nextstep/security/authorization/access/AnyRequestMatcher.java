package nextstep.security.authorization.access;

import jakarta.servlet.http.HttpServletRequest;

public class AnyRequestMatcher implements RequestMatcher {

    @Override
    public boolean matches(HttpServletRequest request) {
        return true;
    }
}
