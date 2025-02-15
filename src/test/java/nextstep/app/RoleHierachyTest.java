package nextstep.app;

import nextstep.security.authorization.NullRoleHierarchy;
import nextstep.security.authorization.RoleHierarchyImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RoleHierachyTest {

    @DisplayName("상위 권한의 reachable authorities는 사용자의 권한과 하위 권한을 같이 반환한다.")
    @Test
    public void roleHierarchyTest() {

        // given
        RoleHierarchyImpl roleHierarchy = RoleHierarchyImpl.with()
                .role("ADMIN").implies("USER")
                .build();

        Set<String> authorites = new HashSet<>();
        authorites.add("ADMIN");

        // when
        Collection<String> reachableGrantedAuthorities = roleHierarchy.getReachableGrantedAuthorities(authorites);

        // then
        Assertions.assertThat(reachableGrantedAuthorities).containsOnly("ADMIN", "USER");
    }

    @DisplayName("NullRoleHierarchy의 reachable authorities는 사용자의 권한 리스트를 그대로 반환한다.")
    @Test
    public void nullRoleHierarchyTest() {

        // given
        NullRoleHierarchy nullRoleHierarchy = new NullRoleHierarchy();

        Set<String> authorites = new HashSet<>();
        authorites.add("ADMIN");
        authorites.add("USER");

        // when
        Collection<String> result = nullRoleHierarchy.getReachableGrantedAuthorities(authorites);

        // then
        Assertions.assertThat(result).isSameAs(authorites);
    }
}
