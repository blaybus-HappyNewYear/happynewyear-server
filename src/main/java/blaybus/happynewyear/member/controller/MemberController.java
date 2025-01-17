package blaybus.happynewyear.member.controller;

import blaybus.happynewyear.member.dto.*;
import blaybus.happynewyear.member.jwt.JwtTokenProvider;
import blaybus.happynewyear.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

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

    @PostMapping("/sign-out")
    public ResponseEntity<String > signOut(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        memberService.signOut(accessToken);

        return ResponseEntity.ok("signed out successfully.");
    }

    @GetMapping("/mypage")
    public ResponseEntity<MemberInfoDto> getMemberInfo(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        MemberInfoDto memberInfoDto = memberService.getMemberInfo(accessToken);
        return ResponseEntity.ok(memberInfoDto);
    }

    @PostMapping("/mypage/password")
    public ResponseEntity<String> updatePassword(HttpServletRequest request, @RequestBody PasswordUpdateDto passwordUpdateDto) {
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        memberService.updatePassword(accessToken, passwordUpdateDto);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }

    @PostMapping("/mypage/character")
    public ResponseEntity<String> updateCharacter(HttpServletRequest request, @RequestParam int imgNumber) {
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        memberService.updateCharacter(accessToken, imgNumber);
        return ResponseEntity.ok("캐릭터가 성공적으로 변경되었습니다");
    }

    @GetMapping("/character-list")
    public ResponseEntity<List<CharacterDto>> getCharacterList() {
        List<CharacterDto> characterList = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            characterList.add(CharacterDto.toDto(i));
        }
        return ResponseEntity.ok(characterList);
    }

    @GetMapping("/get-exp")
    public ResponseEntity<MemberExpDto> getExp(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        MemberExpDto memberExpDto = memberService.getMemberExp(accessToken);
        return ResponseEntity.ok(memberExpDto); // 여기에 MemberExoDto를 반환해 줘야함.
    }


    @GetMapping("/jwt-test")
    public String test() {
        return "success!";}

    @PostMapping("/fcm-token")
    public ResponseEntity<String> updateFcmToken(HttpServletRequest request,
                                                 @RequestBody FcmTokenRequest fcmTokenRequest) {
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        memberService.updateFcmToken(accessToken, fcmTokenRequest.getFcmToken());

        return ResponseEntity.ok("FCM 토큰이 성공적으로 업데이트되었습니다.");
    }

}
