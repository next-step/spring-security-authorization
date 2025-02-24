package nextstep.security.access;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RoleHierarchyImplTest {

    @DisplayName("하위 계층의 모든 역할을 포함시킨다")
    @Test
    public void setHierarchy() throws Exception {
        // given
        final RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();

        // when
        roleHierarchy.setHierarchy("""
                ROLE_ADMIN > ROLE_MANAGER
                ROLE_MASTER > ROLE_MANAGER
                ROLE_MANAGER > ROLE_USER
                """);

        // then
        final Collection<String> actual = roleHierarchy.getReachableGrantedAuthorities(List.of("ROLE_ADMIN"));
        assertThat(actual).hasSize(3)
                .contains("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER");
    }

    @DisplayName("하위 계층의 모든 역할을 순환 관계로 포함시킨다")
    @Test
    public void setHierarchyCircular() throws Exception {
        // given
        final RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();

        // when
        roleHierarchy.setHierarchy("""
                ROLE_ADMIN > ROLE_MANAGER
                ROLE_MANAGER > ROLE_USER
                ROLE_USER > ROLE_ADMIN
                """);

        // then
        final Collection<String> actual1 = roleHierarchy.getReachableGrantedAuthorities(List.of("ROLE_ADMIN"));
        final Collection<String> actual2 = roleHierarchy.getReachableGrantedAuthorities(List.of("ROLE_MANAGER"));
        final Collection<String> actual3 = roleHierarchy.getReachableGrantedAuthorities(List.of("ROLE_USER"));

        assertAll(
                () -> {
                    assertThat(actual1).hasSize(3)
                            .contains("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER");
                },
                () -> {
                    assertThat(actual2).hasSize(3)
                            .contains("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER");
                },
                () -> {
                    assertThat(actual3).hasSize(3)
                            .contains("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER");
                }
        );

    }
}
