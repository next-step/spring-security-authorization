package nextstep.security.authorization;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RoleHierarchyImplTest {
    @Test
    @DisplayName("'>'구분자 없이 역할 계층구조 세팅시 예외발생")
    void exception_on_role_hierarchy_setup_without_delimiter() {

        assertThrows(IllegalArgumentException.class, () -> {
            RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl("ROLE_ADMIN,ROLE_USER");
        });
    }

    @Test
    @DisplayName("한줄로 여러개의 역할을 계층형으로 세팅")
    void setHierarchy_singleLine_multipleRoles() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl("ADMIN > USER > GUEST");

        // ADMIN 역할
        Set<String> adminReachable = roleHierarchy.getReachableRoleAuthorities("ADMIN");
        Assertions.assertEquals(Set.of("ADMIN", "USER", "GUEST"), adminReachable);

        // USER 역할
        Set<String> userReachable = roleHierarchy.getReachableRoleAuthorities("USER");
        Assertions.assertEquals(Set.of("USER", "GUEST"), userReachable);

        // GUEST 역할
        Set<String> guestReachable = roleHierarchy.getReachableRoleAuthorities("GUEST");
        Assertions.assertEquals(Set.of("GUEST"), guestReachable);
    }

    @Test
    @DisplayName("순환참조시 예외발생")
    void circular_reference_throw_exception() {
        // 순환참조가 있는 계층:
        assertThrows(IllegalArgumentException.class, () -> {
            new RoleHierarchyImpl("ADMIN > USER\nUSER > GUEST\nGUEST > ADMIN");
        });
    }
}
