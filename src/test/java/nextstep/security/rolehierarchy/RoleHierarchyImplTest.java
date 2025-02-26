package nextstep.security.rolehierarchy;

import nextstep.security.authorization.GrantedAuthority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RoleHierarchyImplTest {


    @Test
    @DisplayName("role은 implies의 권한을 포함한다")
    void successTest() {
        var roleHierarchyImpl = RoleHierarchyImpl.with()
                .role(TestGrantedAuthority.ROLE_A)
                .implies(TestGrantedAuthority.ROLE_B)
                .build();

        var roles = roleHierarchyImpl.getReachableGrantedAuthorities(List.of(TestGrantedAuthority.ROLE_A));
        assertThat(roles).containsOnly(
                TestGrantedAuthority.ROLE_A,
                TestGrantedAuthority.ROLE_B);
    }

    @Test
    @DisplayName("implies는 role 의 권한을 포함하지 않는다")
    void successTest2() {
        var roleHierarchyImpl = RoleHierarchyImpl.with()
                .role(TestGrantedAuthority.ROLE_A)
                .implies(TestGrantedAuthority.ROLE_B)
                .build();
        var roles = roleHierarchyImpl.getReachableGrantedAuthorities(List.of(TestGrantedAuthority.ROLE_B));
        assertThat(roles).containsOnly(
                TestGrantedAuthority.ROLE_B);

    }

    @Test
    @DisplayName("계층구조가 설정된 게 없으면 본인을 반환한다.")
    void successTest3() {
        var roleHierarchyImpl = RoleHierarchyImpl.with()
                .role(TestGrantedAuthority.ROLE_A)
                .implies(TestGrantedAuthority.ROLE_B)
                .build();
        var roles = roleHierarchyImpl
                .getReachableGrantedAuthorities(List.of(TestGrantedAuthority.ROLE_C));
        assertThat(roles).containsOnly(
                TestGrantedAuthority.ROLE_C);

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

