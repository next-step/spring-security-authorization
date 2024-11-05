package nextstep.security.authorization.matcher;

import javax.servlet.http.HttpServletRequest;

/**
 * 특정 요청에 따라 일치하는지 검증하기 위한 객체
 */
public interface RequestMatcher {

    boolean matches(HttpServletRequest request);
}
