package nextstep.app.ui;

import java.util.List;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.dto.LoginUser;
import nextstep.security.exception.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping
    public ResponseEntity<List<Member>> list() {
        List<Member> members = memberRepository.findAll();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/me")
    public ResponseEntity<Member> me(LoginUser loginUser) {
        final Member member = memberRepository.findByEmail(loginUser.getEmail())
            .orElseThrow(() -> new AuthenticationException());
        return ResponseEntity.ok(member);
    }
}
