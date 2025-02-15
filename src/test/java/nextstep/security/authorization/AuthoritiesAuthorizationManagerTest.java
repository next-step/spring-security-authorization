package nextstep.security.authorization;

import nextstep.security.fixture.TestAuthentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class AuthoritiesAuthorizationManagerTest {
    private final RoleHierarchy nullRoleHierarchy = new NullRoleHierarchy();

    @DisplayName("단건의 인가 정보를 확인 할 수 있다")
    @Test
    void one_role_granted() {
        // given
        final Set<String> authorities = Set.of("USER");
        AuthoritiesAuthorizationManager authorizationManager = new AuthoritiesAuthorizationManager(nullRoleHierarchy);

        // when
        AuthorizationDecision userDecision = authorizationManager.check(TestAuthentication.user(), authorities);
        AuthorizationDecision adminDecision = authorizationManager.check(TestAuthentication.admin(), authorities);

        // then
        assertSoftly(it -> {
            it.assertThat(userDecision.isGranted()).isTrue();
            it.assertThat(adminDecision.isGranted()).isFalse();
        });

    }

    @DisplayName("여러건의 인가 정보를 확인 할 수 있다")
    @Test
    public void many_role_granted() {
        // given
        final Set<String> authorities = Set.of("USER", "ADMIN");
        AuthoritiesAuthorizationManager manager = new AuthoritiesAuthorizationManager(nullRoleHierarchy);

        // when
        AuthorizationDecision userDecision = manager.check(TestAuthentication.admin(), authorities);
        AuthorizationDecision adminDecision = manager.check(TestAuthentication.admin(), authorities);
        AuthorizationDecision denyDecision = manager.check(TestAuthentication.authenticated("authenticated"), authorities);

        // then
        assertSoftly(it -> {
            it.assertThat(userDecision.isGranted()).isTrue();
            it.assertThat(adminDecision.isGranted()).isTrue();
            it.assertThat(denyDecision.isDeny()).isTrue();
        });
    }

    @DisplayName("roleHierarchy 를 설정하면 같은 계층에 맞는 인가를 허용한다")
    @Test
    public void roleHierarchy() {
        // given
        RoleHierarchy roleHierarchy = RoleHierarchyImpl.with()
                .role("ADMIN")
                .implies("USER")
                .build();

        final Set<String> authorities = Set.of("USER");

        AuthoritiesAuthorizationManager manager = new AuthoritiesAuthorizationManager(roleHierarchy);

        // when
        AuthorizationDecision userDecision = manager.check(TestAuthentication.user(), authorities);
        AuthorizationDecision adminDecision = manager.check(TestAuthentication.admin(), authorities);

        // then
        assertSoftly(it -> {
            it.assertThat(userDecision.isGranted()).isTrue();
            it.assertThat(adminDecision.isGranted()).isTrue();
        });
    }

    @DisplayName("roleHierarchy 를 설정하고 다건의 인가를 설정하면 계층관계와 이외의 인가정보를 확인한다.")
    @Test
    public void roleHierarchy_manyRoles() {
        // given
        RoleHierarchy roleHierarchy = RoleHierarchyImpl.with()
                .role("ADMIN")
                .implies("USER")
                .build();

        final Set<String> authorities = Set.of("USER", "GUEST");

        AuthoritiesAuthorizationManager manager = new AuthoritiesAuthorizationManager(roleHierarchy);

        // when
        AuthorizationDecision userDecision = manager.check(TestAuthentication.user(), authorities);
        AuthorizationDecision adminDecision = manager.check(TestAuthentication.admin(), authorities);
        AuthorizationDecision guestDecision = manager.check(TestAuthentication.authenticated("GUEST"), authorities);


        // then
        assertSoftly(it -> {
            it.assertThat(userDecision.isGranted()).isTrue();
            it.assertThat(adminDecision.isGranted()).isTrue();
            it.assertThat(guestDecision.isGranted()).isTrue();
        });
    }
}
