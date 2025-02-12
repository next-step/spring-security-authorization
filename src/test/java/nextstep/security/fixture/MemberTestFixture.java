package nextstep.security.fixture;

import java.util.Base64;
import java.util.Set;
import nextstep.app.domain.Member;

public class MemberTestFixture {
    public static final Member TEST_ADMIN_MEMBER = new Member("a@a.com", "password", "a", "", Set.of("ADMIN"));
    public static final Member TEST_USER_MEMBER = new Member("b@b.com", "password", "b", "", Set.of("USER"));

    public static String createAdminToken(){
        return Base64.getEncoder().encodeToString((TEST_ADMIN_MEMBER.getEmail() + ":" + TEST_ADMIN_MEMBER.getPassword()).getBytes());
    }

    public static String createMemberToken(){
        return Base64.getEncoder().encodeToString((TEST_USER_MEMBER.getEmail() + ":" + TEST_USER_MEMBER.getPassword()).getBytes());
    }
}
