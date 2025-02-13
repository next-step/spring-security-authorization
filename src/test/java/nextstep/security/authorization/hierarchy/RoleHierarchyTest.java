package nextstep.security.authorization.hierarchy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RoleHierarchyTest {

    private RoleHierarchyImpl roleHierarchy;

    @BeforeEach
    void setUp() {
        roleHierarchy = RoleHierarchyImpl.with()
                .role("ADMIN").implies("USER")
                .role("USER").implies("GUEST")
                .build();
    }

    @Nested
    @DisplayName("build()")
    class build {
        @Test
        @DisplayName("계층 설정 시 순환참조가 존재하면 예외를 발생한다.")
        void shouldReturnSameRole_WhenNoHierarchy() {
            assertThatThrownBy(() ->
                    RoleHierarchyImpl.with()
                            .role("ADMIN").implies("USER")
                            .role("USER").implies("GUEST")
                            .role("GUEST").implies("ADMIN")
                            .build()
            ).isInstanceOf(CycleInRoleHierarchyException.class);
        }

    }

    @Nested
    @DisplayName("getReachableAuthorities()")
    class GetReachableAuthorities {

        @Test
        @DisplayName("계층이 없는 경우, 입력한 역할 그대로 반환한다.")
        void shouldReturnSameRole_WhenNoHierarchy() {
            RoleHierarchy singleHierarchy = new NullRoleHierarchy();

            Collection<String> reachableRoles = singleHierarchy.getReachableAuthorities(Set.of("ADMIN"));

            assertThat(reachableRoles).containsExactlyInAnyOrder("ADMIN");
        }

        @Test
        @DisplayName("ADMIN이 주어지면 USER, GUEST까지 포함하여 반환한다.")
        void shouldReturnAllReachableRoles_WhenAdminIsGiven() {
            Collection<String> reachableRoles = roleHierarchy.getReachableAuthorities(Set.of("ADMIN"));

            assertThat(reachableRoles).containsExactlyInAnyOrder("ADMIN", "USER", "GUEST");
        }

        @Test
        @DisplayName("USER가 주어지면 GUEST까지 포함하여 반환한다.")
        void shouldReturnReachableRoles_WhenUserIsGiven() {
            Collection<String> reachableRoles = roleHierarchy.getReachableAuthorities(Set.of("USER"));

            assertThat(reachableRoles).containsExactlyInAnyOrder("USER", "GUEST");
        }

        @Test
        @DisplayName("GUEST가 주어지면 본인만 반환한다.")
        void shouldReturnSelf_WhenGuestIsGiven() {
            Collection<String> reachableRoles = roleHierarchy.getReachableAuthorities(Set.of("GUEST"));

            assertThat(reachableRoles).containsExactlyInAnyOrder("GUEST");
        }

        @Test
        @DisplayName("여러 개의 역할이 주어지면 계층에 포함된 모든 역할을 반환한다.")
        void shouldHandleMultipleRoles() {
            Collection<String> reachableRoles = roleHierarchy.getReachableAuthorities(Set.of("ADMIN", "GUEST"));

            assertThat(reachableRoles).containsExactlyInAnyOrder("ADMIN", "USER", "GUEST");
        }

        @Test
        @DisplayName("빈 리스트를 입력하면 빈 리스트 반환")
        void shouldReturnEmptyList_WhenNoRolesAreProvided() {
            Collection<String> reachableRoles = roleHierarchy.getReachableAuthorities(Set.of());

            assertThat(reachableRoles).isEmpty();
        }

        @Test
        @DisplayName("NULL 입력 시 빈 리스트 반환")
        void shouldReturnEmptyList_WhenNullIsProvided() {
            Collection<String> reachableRoles = roleHierarchy.getReachableAuthorities(null);

            assertThat(reachableRoles).isEmpty();
        }
    }

}