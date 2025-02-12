package nextstep.security.authorization;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestAuthorizationManagerTest {

    @Test
    @DisplayName("특정 정책에서 인증이 되지 않아 실패를 반환합니다.")
    void exception() {
        //given
        final AuthorizationManager<HttpServletRequest> authorizationManager = new AuthenticatedAuthorizationManager();
        final List<RequestMatcherEntry<AuthorizationManager>> entries = List.of(new RequestMatcherEntry<>(new AnyRequestMatcher(), authorizationManager)
                , new RequestMatcherEntry<>(new AnyRequestMatcher(), new PermitAllAuthorizationManager()));
        final RequestAuthorizationManager manager = new RequestAuthorizationManager(entries);

        final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/temp");
        final Authentication authentication = null;

        //when & then
        assertThrows(AuthenticationException.class, () -> manager.check(authentication, request));
    }

    @Test
    @DisplayName("특정 정책에서 인증은 되었지만, 권한이 없어 false를 반환합니다.")
    void isFalse() {
        //given
        final AuthorizationManager<HttpServletRequest> authorizationManager = new AuthorityAuthorizationManager("USER");
        final List<RequestMatcherEntry<AuthorizationManager>> entries = List.of(new RequestMatcherEntry<>(new AnyRequestMatcher(), authorizationManager)
                , new RequestMatcherEntry<>(new AnyRequestMatcher(), authorizationManager));
        final RequestAuthorizationManager manager = new RequestAuthorizationManager(entries);

        final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/temp");
        final Authentication authentication = UsernamePasswordAuthenticationToken.authenticated("id", "password", Set.of("ADMIN"));

        //when
        final boolean result = manager.check(authentication, request).result;

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("인증 및 유효한 권한 정책을 통과하면, true를 반환합니다.")
    void isTrue() {
        //given
        final AuthorizationManager<HttpServletRequest> authorizationManager = new AuthorityAuthorizationManager("ADMIN");
        final List<RequestMatcherEntry<AuthorizationManager>> entries = List.of(new RequestMatcherEntry<>(new AnyRequestMatcher(), authorizationManager)
                , new RequestMatcherEntry<>(new AnyRequestMatcher(), authorizationManager));
        final RequestAuthorizationManager manager = new RequestAuthorizationManager(entries);

        final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/temp");
        final Authentication authentication = UsernamePasswordAuthenticationToken.authenticated("id", "password", Set.of("ADMIN"));

        //when
        final boolean result = manager.check(authentication, request).result;

        //then
        assertThat(result).isTrue();
    }
}
