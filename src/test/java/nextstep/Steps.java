package nextstep;

import nextstep.app.domain.MemberRepository;

import static nextstep.Fixture.TEST_ADMIN_MEMBER;
import static nextstep.Fixture.TEST_USER_MEMBER;

public final class Steps {
    private Steps() {}

    public static void setUp(
            final MemberRepository memberRepository
    ) {
        memberRepository.save(TEST_ADMIN_MEMBER);
        memberRepository.save(TEST_USER_MEMBER);
    }

}
