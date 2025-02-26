package nextstep.app;

import nextstep.app.domain.Member;
import nextstep.app.domain.Role;

import java.util.Set;

public class MemberFixture {
    public static final Member TEST_ADMIN_MEMBER = new Member("a@a.com", "password", "a", "", Set.of(Role.ADMIN));
    public static final Member TEST_USER_MEMBER = new Member("b@b.com", "password", "b", "", Set.of());

}
