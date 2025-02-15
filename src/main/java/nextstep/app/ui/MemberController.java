package nextstep.app.ui;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.exception.UserNotFoundException;
import nextstep.security.authentication.Authentication;
import nextstep.security.authorization.Secured;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<Member> get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        return ResponseEntity.ok(member);
    }

    @Secured("ADMIN")
    @GetMapping("/search")
    public ResponseEntity<List<Member>> search() {
        List<Member> members = memberRepository.findAll();
        return ResponseEntity.ok(members);
    }
}
