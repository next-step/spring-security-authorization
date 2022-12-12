package nextstep.app.ui;

import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.dto.MemberDto;
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
    public ResponseEntity<List<MemberDto>> list() {
        final var members = memberRepository.findAll();
        return ResponseEntity.ok(MemberDto.toList(members));
    }
}
