package nextstep.app.ui;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.authorization.Secured;
import nextstep.security.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MemberController {

    private final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/members")
    public ResponseEntity<List<Member>> list() {
        List<Member> members = memberRepository.findAll();
        return ResponseEntity.ok(members);
    }

    @Secured("ADMIN")
    @GetMapping("/search")
    public ResponseEntity<List<Member>> search() {
        List<Member> members = memberRepository.findAll();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/members/me")
    public Member findMe() {
        final String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다"));
    }
}
