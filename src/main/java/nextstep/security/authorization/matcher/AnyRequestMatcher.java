package nextstep.security.authorization.matcher;

import javax.servlet.http.HttpServletRequest;

public class AnyRequestMatcher implements RequestMatcher {

    @Override
    public boolean matches(HttpServletRequest request) {
        return true;
    }

}
