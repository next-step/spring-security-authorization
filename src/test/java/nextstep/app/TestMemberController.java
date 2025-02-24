package nextstep.app;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
    class TestMemberController {

        private final MemberRepository memberRepository;

        public TestMemberController(final MemberRepository memberRepository) {
            this.memberRepository = memberRepository;
        }

        @GetMapping("/members/managers")
        public ResponseEntity<List<Member>> managers() {
            List<Member> members = memberRepository.findAll()
                    .stream().filter(member -> member.getRoles().contains("MANAGER"))
                    .toList();
            return ResponseEntity.ok(members);
        }
    }
