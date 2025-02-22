package nextstep.security.access;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

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

}
