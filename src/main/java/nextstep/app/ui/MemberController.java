package nextstep.app.ui;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.aspect.Secured;
import nextstep.security.authentication.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    // 해당 API 가 ADMIN 권한을 가진 사용자만 호출할 수 있다.
    @Secured("ADMIN")
    @GetMapping("/search")
    public ResponseEntity<List<Member>> search() {
        List<Member> members = memberRepository.findAll();
        return ResponseEntity.ok(members);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handleAuthenticationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
