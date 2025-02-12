package nextstep;

import nextstep.app.domain.Member;

import java.util.Set;

public enum MemberFixture {
    TEST_ADMIN_MEMBER(new Member(
            "a@a.com", "password",
            "a", "",
            Set.of("USER", "ADMIN")
    )),
    TEST_USER_MEMBER(new Member(
            "b@b.com", "password",
            "b", "",
            Set.of("USER")
    ));

    private final Member member;

    MemberFixture(Member member) {
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

    public String getEmail() {
        return member.getEmail();
    }

    public String getPassword() {
        return member.getPassword();
    }
}
