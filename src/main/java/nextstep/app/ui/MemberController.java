package nextstep.app.ui;

import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.dto.MemberDto;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.exception.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MemberController {

    private final MemberRepository memberRepository;
    public MemberController(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberDto>> list() {
        final var members = memberRepository.findAll();
        return ResponseEntity.ok(MemberDto.toList(members));
    }

    @GetMapping("/members/me")
    public ResponseEntity<MemberDto> me() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        final var email = (String) authentication.getPrincipal();

        final var member = memberRepository.findByEmail(email)
                .orElseThrow(AuthenticationException::new);

        return ResponseEntity.ok(MemberDto.toDto(member));
    }
}
