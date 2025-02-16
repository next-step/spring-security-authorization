package nextstep.security.authorization;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class RoleHierarchyImplTest {

    @DisplayName("계층별로 허용되는 권한을 반환한다")
    @ParameterizedTest
    @MethodSource("계층별로_허용되는_권한")
    void reachableGrantedAuthorities(String role, Set<String> grantedAuthorities) {
        RoleHierarchyImpl roleHierarchy = RoleHierarchyImpl.with()
                .role("ADMIN")
                .implies("USER","GUEST")
                .build();

        Collection<String> reachableGrantedAuthorities = roleHierarchy.getReachableGrantedAuthorities(List.of(role));

        assertThat(reachableGrantedAuthorities).isEqualTo(grantedAuthorities);
    }

    private static Stream<Arguments> 계층별로_허용되는_권한() {
        return Stream.of(
                Arguments.of("ADMIN", Set.of("ADMIN", "USER","GUEST")),
                Arguments.of("USER", Set.of("USER", "GUEST")),
                Arguments.of("GUEST", Set.of("GUEST"))
        );
    }
}
