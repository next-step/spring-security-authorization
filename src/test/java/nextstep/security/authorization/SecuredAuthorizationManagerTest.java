package nextstep.security.authorization;

import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.UsernamePasswordAuthenticationToken;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecuredAuthorizationManagerTest {

    private MethodInvocation mockInvocation;

    @BeforeEach
    void init() {
        mockInvocation = mock(MethodInvocation.class);
    }

    @Nested
    @DisplayName("인증에 이미 성공하고,")
    class authentication {

        @Nested
        @DisplayName("ADMIN 권한을 가진 회원이 요청했을 때,")
        class adminRoleRequest {

            @Test
            @DisplayName("해당 API의 허용 권한이 ADMIN이면, true를 반환합니다.")
            void isTrue() throws NoSuchMethodException {
                //given
                final String role = "ADMIN";
                final AuthorizationManager<MethodInvocation> authorizationManager = new SecuredAuthorizationManager();

                final Authentication authentication = UsernamePasswordAuthenticationToken.authenticated("id", "password", Set.of(role));
                final Method securedMethod = AdminTest.class.getMethod("admin");
                when(mockInvocation.getMethod()).thenReturn(securedMethod);

                //when
                final boolean result = authorizationManager.check(authentication, mockInvocation).result;

                //then
                assertThat(result).isTrue();
            }

            @Test
            @DisplayName("해당 API의 허용 권한이 ADMIN이 아니면, false를 반환합니다.")
            void isFalse() throws NoSuchMethodException {
                //given
                final String role = "ADMIN";
                final AuthorizationManager<MethodInvocation> authorizationManager = new SecuredAuthorizationManager();

                final Authentication authentication = UsernamePasswordAuthenticationToken.authenticated("id", "password", Set.of(role));
                final Method securedMethod = UserTest.class.getMethod("user");
                when(mockInvocation.getMethod()).thenReturn(securedMethod);

                //when
                final boolean result = authorizationManager.check(authentication, mockInvocation).result;

                //then
                assertThat(result).isFalse();
            }
        }
    }

    @Nested
    @DisplayName("인증이 되지 않았을 때,")
    class unauthentication {

        @Test
        @DisplayName("해당 API의 허용 권한이 ADMIN이면, false 반환합니다.")
        void isFalse() throws NoSuchMethodException {
            //given
            final AuthorizationManager<MethodInvocation> authorizationManager = new SecuredAuthorizationManager();

            final Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated("id", "password");
            final Method securedMethod = AdminTest.class.getMethod("admin");
            when(mockInvocation.getMethod()).thenReturn(securedMethod);

            //when
            final boolean result = authorizationManager.check(authentication, mockInvocation).result;

            //then
            assertThat(result).isFalse();
        }
    }

    static class AdminTest {
        @Secured("ADMIN")
        public void admin() {
        }
    }

    static class UserTest {
        @Secured("USER")
        public void user() {
        }
    }
}
