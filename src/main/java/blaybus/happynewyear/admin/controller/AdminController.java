package blaybus.happynewyear.admin.controller;


import blaybus.happynewyear.admin.dto.MemberDetailDto;
import blaybus.happynewyear.admin.dto.MemberListDto;
import blaybus.happynewyear.admin.service.AdminService;
import blaybus.happynewyear.member.dto.SignUpDto;
import blaybus.happynewyear.member.jwt.JwtTokenProvider;
import blaybus.happynewyear.post.dto.CreatePostDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 관리자 계정 생성
    @PostMapping("/create-admin")
    public ResponseEntity<String> createAdmin(@RequestBody SignUpDto signUpDto) {
        adminService.createAdmin(signUpDto);
        return ResponseEntity.ok("관리자 계정 생성이 완료되었습니다");
    }

    // 게시글 작성
    @PostMapping("/admin/create-post")
    public ResponseEntity<String> createPost(@RequestBody CreatePostDto createPostDto) {
        adminService.createPost(createPostDto);
        return ResponseEntity.ok("게시글 작성이 완료되었습니다");
    }

    // 회원 추가
    @PostMapping("/admin/create-member")
    public ResponseEntity<String> createMember(@RequestBody SignUpDto signUpDto) {
        adminService.createMember(signUpDto);
        return ResponseEntity.ok("회원 추가가 완료되었습니다");
    }

    // 모든 회원 정보 불러오기
    @GetMapping("/admin/get-member-list")
    public ResponseEntity<List<MemberListDto>> getMemberList() {
        List<MemberListDto> memberList = adminService.getMemberList();
        return ResponseEntity.ok(memberList);
    }

    // 개별 회원 정보 리턴
    @GetMapping("/admin/get-member-detail/{id}")
    public ResponseEntity<MemberDetailDto> getMemberDetail(@PathVariable Long id) {
        MemberDetailDto memberDetailDto = adminService.getMemberDetail(id);
        return ResponseEntity.ok(memberDetailDto);
    }

    // 회원 정보 수정하기
    @PostMapping("/admin/edit-member/{id}")
    public ResponseEntity<MemberDetailDto> editMemberDetail(@PathVariable Long id, @RequestBody MemberDetailDto memberDetailDto) {
        MemberDetailDto editDetailDto = adminService.editMemberDetail(id, memberDetailDto);
        return ResponseEntity.ok(editDetailDto);
    }
}
