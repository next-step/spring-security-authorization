package nextstep.app.ui;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.authentication.Authentication;
import nextstep.security.authentication.AuthenticationException;
import nextstep.security.authorization.Secured;
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

    @Secured("ADMIN")
    @GetMapping("/search")
    public ResponseEntity<List<Member>> search() {
        List<Member> members = memberRepository.findAll();
        return ResponseEntity.ok(members);
    }

    @Secured("USER")
    @GetMapping("/members/me")
    public ResponseEntity<Member> me() {
        final Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        return ResponseEntity.ok(
                Optional.ofNullable(authentication)
                        .flatMap(this::findMe)
                        .orElseThrow(AuthenticationException::new)
        );
    }

    private Optional<Member> findMe(Authentication authentication) {
        return memberRepository.findByEmail(
                authentication.getPrincipal().toString()
        );
    }
}
