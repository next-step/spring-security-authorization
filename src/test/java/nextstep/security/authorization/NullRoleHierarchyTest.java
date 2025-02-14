package nextstep.security.authorization;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NullRoleHierarchyTest {

    @Test
    @DisplayName("nullRoleHierarchy 권한을 그대로 반환한다")
    void nullRoleHierarchy() {
        NullRoleHierarchy nullRoleHierarchy = new NullRoleHierarchy();

        Collection<String> reachableGrantedAuthorities = nullRoleHierarchy.getReachableGrantedAuthorities(List.of("ADMIN", "USER"));

        assertThat(reachableGrantedAuthorities).contains("ADMIN", "USER");
    }

}
