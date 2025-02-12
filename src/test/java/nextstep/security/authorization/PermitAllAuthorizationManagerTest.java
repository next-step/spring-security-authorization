package nextstep.security.authorization;


import nextstep.security.authentication.Authentication;
import nextstep.security.fixture.TestAuthentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PermitAllAuthorizationManagerTest {

    @ParameterizedTest
    @DisplayName("어떠한 인증이여도 허용한다")
    @MethodSource("모든_케이스에_인증_정보")
    void all_pass(Authentication authentication) {
        PermitAllAuthorizationManager manager = new PermitAllAuthorizationManager();

        AuthorizationDecision check = manager.check(authentication, new MockHttpServletRequest());

        assertThat(check.isDeny()).isFalse();
    }

    private static Stream<Arguments> 모든_케이스에_인증_정보() {
        return Stream.of(
                Arguments.of(TestAuthentication.authenticated()),
                Arguments.of(TestAuthentication.user()),
                Arguments.of(TestAuthentication.admin()),
                Arguments.of(TestAuthentication.unAuthenticated())
        );
    }
}
