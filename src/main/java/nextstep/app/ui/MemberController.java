package nextstep.app.ui;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.authorization.Secured;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/members/me")
    public ResponseEntity<Member> getMember() {
        SecurityContext context = SecurityContextHolder.getContext();
        String email = (String) context.getAuthentication().getPrincipal();

        Optional<Member> memberOpt = memberRepository.findByEmail(email);
        if (memberOpt.isEmpty()) {
            throw new IllegalStateException("회원을 찾을 수 없습니다. (email=%s)".formatted(email));
        }

        return ResponseEntity.ok(memberOpt.get());
    }

    @Secured("ADMIN")
    @GetMapping("/search")
    public ResponseEntity<List<Member>> search() {
        List<Member> members = memberRepository.findAll();
        return ResponseEntity.ok(members);
    }
}
