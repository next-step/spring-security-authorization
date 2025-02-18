package nextstep.security.authorization.role;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoleHierarchyTest {
    private RoleHierarchy hierarchy;

    @DisplayName("하위의 Role 을 탐색할 수 있어야 한다.")
    @Test
    void lowerRoles() {
        hierarchy = new RoleHierarchyBuilder()
                .role("ADMIN").implies("MANAGER")
                .role("MANAGER").implies("USER1", "USER2")
                .build();
        assertThat(hierarchy.getReachableGrantedAuthorities(
                GrantedAuthority.setOf("ADMIN")
        ).stream().map(GrantedAuthority::getAuthority))
                .containsExactlyInAnyOrder(
                        "ADMIN",
                        "MANAGER",
                        "USER1",
                        "USER2"
                );
    }

    @DisplayName("Role 이 순환참조를 할 경우 Hierarchy 생성이 불가능해야 한다.")
    @Test
    void circular() {
        RoleHierarchyBuilder builder = new RoleHierarchyBuilder()
                .role("ADMIN").implies("MANAGER")
                .role("MANAGER").implies("USER")
                .role("USER").implies("ADMIN");
        assertThrows(CircularRoleException.class, builder::build);
    }

    @DisplayName("RoleHierarchy 를 설정하지 않았을 경우에는 NullRoleHierarchy 를 사용한다.")
    @Test
    void nullRoleHierarchy() {
        hierarchy = new RoleHierarchyBuilder().build();
        assertAll(
                () -> assertThat(hierarchy)
                        .isEqualTo(NullRoleHierarchy.getInstance()),
                () -> assertThat(
                        hierarchy.getReachableGrantedAuthorities(
                                GrantedAuthority.setOf("ADMIN")
                        ).stream().map(GrantedAuthority::getAuthority)
                ).containsExactlyInAnyOrder("ADMIN")
        );
    }
}
