package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;

import java.util.List;
import java.util.Set;

/**
 * - 요청에 대해서 적합한 권한이 있는지만 판단.
 * - 인가가 되지 않았을 경우 예외를 발생시키지는 않는다. (예외는 AuthorizationFilter에서 던짐)
 */
public class RequestAuthorizationManager implements AuthorizationManager<HttpServletRequest> {

    private static final List<String> PUBLIC_URIS = List.of("/login", "/search");

    @Override
    public AuthorizationDecision check(Authentication authentication, HttpServletRequest request) {
        if (PUBLIC_URIS.contains(request.getRequestURI())) {
            return AuthorizationDecision.grantedOf();
        }

        if (!isAuthenticated(authentication)) { // PUBLIC_URIS에 포함되지 않은 URI는 모두 인증 필요.
            return AuthorizationDecision.deniedOf();
        }

        return checkAuthorityByUri(authentication, request.getRequestURI());
    }

    private AuthorizationDecision checkAuthorityByUri(Authentication authentication, String requestURI) {
        if (requestURI.equals("/members/me")) { // 인증된 사용자 허용
            return AuthorizationDecision.grantedOf();
        }

        if (requestURI.equals("/members")) { // ADMIN만 허용
            Set<String> authorities = authentication.getAuthorities();
            if (authorities.contains("ADMIN")) {
                return AuthorizationDecision.grantedOf();
            }
            return AuthorizationDecision.deniedOf();
        }

        return AuthorizationDecision.deniedOf();
    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }
}
