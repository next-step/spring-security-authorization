package nextstep.security.role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RoleHierarchyImplTest {

    RoleHierarchyImpl.Builder builder;

    @BeforeEach
    void setUp() {
        builder = RoleHierarchyImpl.builder()
                .role("ADMIN").addImplies("MEMBER");
    }

    @ParameterizedTest
    @ValueSource(strings = {"ADMIN", "MEMBER", "UNKNOWN"})
    @DisplayName("역할 계층을 Authority 로 가져올 수 있다")
    void getRoleHierarchyTest(String roleName) {
        // given
        RoleHierarchyImpl roleHierarchy = builder.build();

        // when
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);
        Collection<GrantedAuthority> reachableRoles = roleHierarchy.getReachableRoles(List.of(authority));

        // then
        List<GrantedAuthority> expect = switch (roleName) {
            case "ADMIN" -> List.of(
                    new SimpleGrantedAuthority("ADMIN"),
                    new SimpleGrantedAuthority("MEMBER")
            );
            case "MEMBER", "UNKNOWN" -> List.of(
                    new SimpleGrantedAuthority(roleName)
            );
            default -> throw new IllegalStateException("Unexpected roleName: " + roleName);
        };

        assertThat(reachableRoles)
                .containsExactlyInAnyOrderElementsOf(expect);
    }

    @Test
    @DisplayName("새로운 역할 계층을 추가할 수 있다.")
    void addRoleHierarchyTest() {
        // given
        String target = "ISRAEL";
        String nothing = "NOTHING";
        String everything = "EVERYTHING";
        RoleHierarchyImpl roleHierarchy = builder.role(target).addImplies(nothing, everything)
                .build();

        // when
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(target);
        Collection<GrantedAuthority> reachableRoles = roleHierarchy.getReachableRoles(List.of(authority));

        // then
        var expect = List.of(
                authority,
                new SimpleGrantedAuthority(nothing),
                new SimpleGrantedAuthority(everything)
        );
        assertThat(reachableRoles).hasSize(3)
                .containsExactlyInAnyOrderElementsOf(expect);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("역할의 이름은 비어있을수 없다")
    void addRoleHierarchyFailTest1(String roleName) {
        assertThatThrownBy(
                () -> builder.role(roleName)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("role must not be null or empty");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("포함된 역할들도 비어있을 수 없다.")
    void addRoleHierarchyFailTest2(String roleName) {
        // given
        String target = "NEW_ROLE";
        RoleHierarchyImpl.Builder.ImpliedRoles impliedRoles = builder.role(target);

        // when
        assertThatThrownBy(
                () -> impliedRoles.addImplies(roleName)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least one implied role must be provided");
    }
}
