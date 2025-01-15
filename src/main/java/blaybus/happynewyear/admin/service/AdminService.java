package blaybus.happynewyear.admin.service;

import blaybus.happynewyear.admin.dto.MemberDetailDto;
import blaybus.happynewyear.admin.dto.MemberListDto;
import blaybus.happynewyear.member.dto.SignUpDto;
import blaybus.happynewyear.post.dto.CreatePostDto;

import java.util.List;

public interface AdminService {

    // 관리자 계정 생성
    void createAdmin(SignUpDto signUpDto);

    // 게시글 작성
    void createPost(CreatePostDto createPostDto);

    // 회원 추가
    void createMember(SignUpDto signUpDto);

    // 모든 회원 정보 불러오기
    List<MemberListDto> getMemberList();

    // 개별 회원 불러오기
    MemberDetailDto getMemberDetail(Long memberId);

    // 회원 정보 수정하기
    MemberDetailDto editMemberDetail(Long memberId, MemberDetailDto memberDetailDto);
}
