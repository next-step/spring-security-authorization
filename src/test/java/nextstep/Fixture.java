package nextstep;

import nextstep.app.domain.Member;

import java.util.Base64;
import java.util.Set;

public final class Fixture {
    public static final Member TEST_ADMIN_MEMBER = new Member(
            "a@a.com", "password",
            "a", "",
            Set.of("USER", "ADMIN")
    );
    public static final Member TEST_USER_MEMBER = new Member(
            "b@b.com", "password",
            "b", "",
            Set.of("USER")
    );
    public static final String TEST_ADMIN_TOKEN = createToken(TEST_ADMIN_MEMBER);
    public static final String TEST_USER_TOKEN = createToken(TEST_USER_MEMBER);

    private Fixture() {}

    private static String createToken(final Member member) {
        return createToken(member.getEmail(), member.getPassword());
    }

    public static String createToken(
            final String principal,
            final String credential
    ) {
        return "Basic " + Base64.getEncoder().encodeToString(
                (principal + ":" + credential).getBytes()
        );
    }
}
