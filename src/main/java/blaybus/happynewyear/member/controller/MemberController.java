package blaybus.happynewyear.member.controller;

import blaybus.happynewyear.member.dto.JwtToken;
import blaybus.happynewyear.member.dto.SignInDto;
import blaybus.happynewyear.member.dto.SignUpDto;
import blaybus.happynewyear.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-in")
    public ResponseEntity<JwtToken> signIn(@RequestBody SignInDto signInDto) {
        String username = signInDto.getUsername();
        String password = signInDto.getPassword();

        JwtToken jwtToken = memberService.signIn(username, password);

        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody SignUpDto signUpDto) {
        memberService.signUp(signUpDto);
        return ResponseEntity.ok("가입이 완료되었습니다.");
    }

}
