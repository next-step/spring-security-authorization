package nextstep.security.authorization.role;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RoleHierarchyTest {
    private RoleHierarchy hierarchy;

    @DisplayName("하위의 Role 을 탐색할 수 있어야 한다.")
    @Test
    void lowerRoles() {
        hierarchy = new RoleHierarchyBuilder()
                .role("ADMIN").implies("MANAGER")
                .role("MANAGER").implies("USER1", "USER2")
                .build();
        assertThat(hierarchy.getReachableGrantedAuthorities(Set.of("ADMIN")))
                .containsExactlyInAnyOrder("ADMIN", "MANAGER", "USER1", "USER2");
    }

    @DisplayName("Role 이 순환참조를 할 경우 탐색을 멈추어야 한다.")
    @Test
    void circular() {
        hierarchy = new RoleHierarchyBuilder()
                .role("ADMIN").implies("MANAGER")
                .role("MANAGER").implies("USER")
                .role("USER").implies("ADMIN")
                .build();
        assertThat(hierarchy.getReachableGrantedAuthorities(Set.of("ADMIN")))
                .containsExactlyInAnyOrder("ADMIN", "MANAGER", "USER");
    }
}
