package nextstep.security.access;

import nextstep.security.core.GrantedAuthority;
import nextstep.security.core.SimpleGrantedAuthority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RoleHierarchyTest {

    @Test
    @DisplayName("역할 계층이 없을 때 입력된 권한만 반환 한다.")
    void getReachableGrantedAuthorities_NoHierarchy() {
        // Given
        RoleHierarchy roleHierarchy = new NullRoleHierarchy();
        Collection<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("USER"));

        // When
        Collection<GrantedAuthority> reachableAuthorities = roleHierarchy.getReachableGrantedAuthorities(authorities);

        // Then
        assertThat(reachableAuthorities).hasSize(1);
        assertThat(reachableAuthorities).contains(new SimpleGrantedAuthority("USER"));
    }

    @Test
    @DisplayName("역할 계층이 존재 시 모든 하위 권한 반환 한다.")
    void getReachableGrantedAuthorities_WithHierarchy() {
        // Given
        RoleHierarchy roleHierarchy = RoleHierarchyImpl.with()
                .role("ADMIN").implies("USER")
                .role("USER").implies("GUEST")
                .build();
        Collection<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ADMIN"));

        // When
        Collection<GrantedAuthority> reachableAuthorities = roleHierarchy.getReachableGrantedAuthorities(authorities);

        // Then
        assertThat(reachableAuthorities).hasSize(3);
        assertThat(reachableAuthorities).containsExactlyInAnyOrder(
                new SimpleGrantedAuthority("ADMIN"),
                new SimpleGrantedAuthority("USER"),
                new SimpleGrantedAuthority("GUEST")
        );
    }

    @Test
    @DisplayName("계층 간 순환 참조 발생 시 예외가 발생한다.")
    void shouldThrowExceptionForCircularRoleReferences() {
        assertThatThrownBy(() -> RoleHierarchyImpl.with()
                .role("ADMIN").implies("USER")
                .role("USER").implies("GUEST")
                .role("GUEST").implies("USER")
                .build())
                .isInstanceOf(CycleInRoleHierarchyException.class);
    }

    @Test
    @DisplayName("빈 권한 리스트 입력 시 빈 결과 반환")
    void getReachableGrantedAuthorities_EmptyInput() {
        // Given
        RoleHierarchy roleHierarchy = RoleHierarchyImpl.with()
                .role("ADMIN").implies("USER")
                .role("USER").implies("GUEST")
                .build();
        // When
        Collection<GrantedAuthority> reachableAuthorities = roleHierarchy.getReachableGrantedAuthorities(Set.of());

        // Then
        assertThat(reachableAuthorities).isEmpty();
    }

    @Test
    @DisplayName("Null 입력 시 빈 결과 반환")
    void getReachableGrantedAuthorities_NullInput() {
        // Given
        RoleHierarchy roleHierarchy = RoleHierarchyImpl.with()
                .role("ADMIN").implies("USER")
                .role("USER").implies("GUEST")
                .build();
        // When
        Collection<GrantedAuthority> reachableAuthorities = roleHierarchy.getReachableGrantedAuthorities(null);

        // Then
        assertThat(reachableAuthorities).isEmpty();
    }
}