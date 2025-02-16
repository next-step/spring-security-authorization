package nextstep.security.rolehierarchy;

import nextstep.security.authorization.GrantedAuthority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RoleHierarchyImplTest {


    @Test
    @DisplayName("successTest")
    void successTest() {
        var roleHierarchyImpl = new RoleHierarchyImpl(Set.of(
                new RoleRelation(TestGrantedAuthority.ROLE_A, TestGrantedAuthority.ROLE_B),
                new RoleRelation(TestGrantedAuthority.ROLE_B, TestGrantedAuthority.ROLE_C),
                new RoleRelation(TestGrantedAuthority.ROLE_C, TestGrantedAuthority.ROLE_D),
                new RoleRelation(TestGrantedAuthority.ROLE_D, TestGrantedAuthority.ROLE_E)
        ));
        var roles = roleHierarchyImpl.getReachableGrantedAuthorities(List.of(TestGrantedAuthority.ROLE_A));
        assertThat(roles).containsOnly(
                TestGrantedAuthority.ROLE_A,
                TestGrantedAuthority.ROLE_B,
                TestGrantedAuthority.ROLE_C,
                TestGrantedAuthority.ROLE_D);
    }

    @Test
    @DisplayName("failTest")
    void failTest() {
        new RoleHierarchyImpl(Set.of(
                new RoleRelation(TestGrantedAuthority.ROLE_A, TestGrantedAuthority.ROLE_B),
                new RoleRelation(TestGrantedAuthority.ROLE_B, TestGrantedAuthority.ROLE_A)
        ))
        ;
    }

    private enum TestGrantedAuthority implements GrantedAuthority {
        ROLE_A,
        ROLE_B,
        ROLE_C,
        ROLE_D,
        ROLE_E,
        ;

        @Override
        public String getAuthority() {
            return this.name();
        }
    }
}

