package nextstep.security.authorization.manager;

import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.AuthorizationDecision;

/**
 * 인증 객체가 있고, 요구되는 권한과 일치하면 통과
 */
public class AuthorityAuthorizationManager<T> implements AuthorizationManager<T> {

    private T authority;

    public AuthorityAuthorizationManager(T authority) {
        this.authority = authority;
    }

    @Override
    public AuthorizationDecision check(Authentication authentication, T object) {

        // 인증이 비어있으면 실패
        if (authentication == null) {
            return new AuthorizationDecision(false);
        }

        // 권한이 비어있으면 실패
        if (authentication.getAuthorities() == null) {
            return new AuthorizationDecision(false);
        }

        // 권한에 필요한 권한이 없으면 실패
        if (!authentication.getAuthorities().contains(authority)) {
            return new AuthorizationDecision(false);
        }

        return new AuthorizationDecision(true);
    }

}