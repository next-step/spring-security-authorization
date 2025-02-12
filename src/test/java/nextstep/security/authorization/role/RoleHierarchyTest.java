package nextstep.security.authorization.role;

import nextstep.security.SimpleGrantedAuthority;
import nextstep.security.authorization.GrantedAuthority;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class RoleHierarchyTest {

    @Test
    void getReachableGrantedAuthorities() {
        RoleHierarchy roleHierarchy = RoleHierarchyImpl.fromHierarchy(
                """
                ROLE_ADMIN > ROLE_USER
                ROLE_USER > ROLE_GUEST
                """
        );

        List<String> reachableAuthorities =
                roleHierarchy.getReachableGrantedAuthorities(Set.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .stream().map(GrantedAuthority::getAuthority).toList();

        Assertions.assertThat(reachableAuthorities).containsExactlyInAnyOrder(
                "ROLE_ADMIN",
                "ROLE_USER",
                "ROLE_GUEST"
        );

    }

    @Test
    void getComplexReachableGrantedAuthorities() {
        RoleHierarchy roleHierarchy = RoleHierarchyImpl.fromHierarchy(
                """
                ROLE_ADMIN > ROLE_USER
                ROLE_USER > ROLE_GUEST
                ROLE_ADMIN > ROLE_ANY
                """
        );

        List<String> reachableAuthorities =
                roleHierarchy.getReachableGrantedAuthorities(Set.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .stream().map(GrantedAuthority::getAuthority).toList();

        Assertions.assertThat(reachableAuthorities).containsExactlyInAnyOrder(
                "ROLE_ADMIN",
                "ROLE_USER",
                "ROLE_GUEST",
                "ROLE_ANY"
        );

    }

    @Test
    void buildWithDsl() {
        RoleHierarchy roleHierarchy = RoleHierarchyImpl.with()
                .role("ROLE_ADMIN").implies("ROLE_USER")
                .role("ROLE_USER").implies("ROLE_GUEST")
                .build();

        List<String> reachableAuthorities =
                roleHierarchy.getReachableGrantedAuthorities(Set.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .stream().map(GrantedAuthority::getAuthority).toList();

        Assertions.assertThat(reachableAuthorities).containsExactlyInAnyOrder(
                "ROLE_ADMIN",
                "ROLE_USER",
                "ROLE_GUEST"
        );
    }
}
