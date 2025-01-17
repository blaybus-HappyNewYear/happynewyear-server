package blaybus.happynewyear.connector.controller;

import blaybus.happynewyear.member.dto.MemberDeleteDto;
import blaybus.happynewyear.member.dto.MemberUpdateDto;
import blaybus.happynewyear.member.dto.SignUpDto;
import blaybus.happynewyear.member.service.MemberService;
import blaybus.happynewyear.post.dto.CreatePostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sheet")
@RequiredArgsConstructor
public class MemberSheetController {

    private final MemberService memberService;

    // 추가 처리
    @PostMapping("/add")
    public ResponseEntity<String> addMember(@RequestBody SignUpDto signUpDto) {
        log.info("team number: {}", signUpDto.getTeamNumber());
        memberService.signUp(signUpDto);
        return ResponseEntity.ok("멤버 추가 성공");

    }

    // 수정 처리
    @PostMapping("/update")
    public ResponseEntity<String> updateMember(@RequestBody MemberUpdateDto memberUpdateDto) {
        memberService.updateMember(memberUpdateDto); // 필요한 경우 추가 데이터를 처리
        return ResponseEntity.ok("멤버 수정 성공");

    }

    // 삭제 처리
    @PostMapping("/delete")
    public ResponseEntity<String> deleteMember(@RequestBody MemberDeleteDto memberDeleteDto) {
        memberService.deleteMember(memberDeleteDto.getUsername());
        return ResponseEntity.ok("멤버 삭제 성공");

    }


}