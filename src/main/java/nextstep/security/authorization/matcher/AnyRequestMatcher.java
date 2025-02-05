package nextstep.security.authorization.matcher;

import jakarta.servlet.http.HttpServletRequest;

public class AnyRequestMatcher implements RequestMatcher {
    private AnyRequestMatcher() {}

    public static AnyRequestMatcher getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return true;
    }

    private static class SingletonHolder {
        private static final AnyRequestMatcher INSTANCE = new AnyRequestMatcher();
    }
}
