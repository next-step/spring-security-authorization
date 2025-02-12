package nextstep;

import nextstep.app.domain.MemberRepository;

public final class Steps {
    private Steps() {}

    public static void setUp(
            final MemberRepository memberRepository
    ) {
        for (var memberFixture : MemberFixture.values()) {
            memberRepository.save(memberFixture.getMember());
        }
    }

}
