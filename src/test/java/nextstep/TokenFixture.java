package nextstep;

import java.util.Base64;

import static nextstep.MemberFixture.TEST_ADMIN_MEMBER;
import static nextstep.MemberFixture.TEST_USER_MEMBER;

public enum TokenFixture {
    TEST_ADMIN_TOKEN(TEST_ADMIN_MEMBER),
    TEST_USER_TOKEN(TEST_USER_MEMBER);

    private final String token;

    TokenFixture(MemberFixture member) {
        this.token = createToken(
                member.getEmail(), member.getPassword()
        );
    }

    public static String createToken(
            final String principal,
            final String credential
    ) {
        return "Basic " + Base64.getEncoder().encodeToString(
                (principal + ":" + credential).getBytes()
        );
    }

    public String getToken() {
        return token;
    }
}
