package blaybus.happynewyear.admin.service.impl;

import blaybus.happynewyear.admin.dto.MemberDetailDto;
import blaybus.happynewyear.admin.dto.MemberListDto;
import blaybus.happynewyear.admin.service.AdminService;
import blaybus.happynewyear.config.error.ErrorCode;
import blaybus.happynewyear.config.error.exception.BusinessException;
import blaybus.happynewyear.member.dto.SignUpDto;
import blaybus.happynewyear.member.entity.Member;
import blaybus.happynewyear.member.entity.Team;
import blaybus.happynewyear.member.jwt.JwtTokenProvider;
import blaybus.happynewyear.member.repository.MemberRepository;
import blaybus.happynewyear.member.repository.TeamRepository;
import blaybus.happynewyear.member.service.MemberService;
import blaybus.happynewyear.notification.service.NotificationService;
import blaybus.happynewyear.post.dto.CreatePostDto;
import blaybus.happynewyear.post.entity.Post;
import blaybus.happynewyear.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final PostRepository postRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final NotificationService notificationService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    // 관리자 계정 생성
    @Override
    @Transactional
    public void createAdmin(SignUpDto signUpDto) {
        memberService.signUp(signUpDto);
        Member member = memberRepository.findByUsername(signUpDto.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        List<String> roles = new ArrayList<>();
        roles.add("USER");  //USER 권한 부여
        roles.add("ADMIN");
        member.setRoles(roles);
    }

    // 게시글 작성
    @Override
    @Transactional
    public void createPost(CreatePostDto createPostDto) {

        LocalDate now = LocalDate.now();
        Post entity = createPostDto.toEntity(now);
        postRepository.save(entity);

        //글 생성 시, 알림 생성
        notificationService.createPostNotification(createPostDto.getTitle());
    }

    // 회원 추가
    @Override
    @Transactional
    public void createMember(SignUpDto signUpDto) {
        memberService.signUp(signUpDto);
    }

    // 회원 정보 불러오기
    @Override
    @Transactional
    public List<MemberListDto> getMemberList() {
        return memberRepository.findAll().stream()
                .map(MemberListDto::toDto)
                .toList();
    }

    // 개별 회원 불러오기
    @Override
    @Transactional
    public MemberDetailDto getMemberDetail(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return MemberDetailDto.toDto(member);
    }

    // 회원 정보 수정하기
    @Override
    @Transactional
    public MemberDetailDto editMemberDetail(Long memberId, MemberDetailDto memberDetailDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Team team = teamRepository.findByTeamNameAndTeamNumber(memberDetailDto.getTeamName(), memberDetailDto.getTeamNumber())
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_NOT_FOUND));

        member.setName(memberDetailDto.getName());
        member.setStartDate(memberDetailDto.getStartDate());
        member.setTeam(team);
        member.setLevel(memberDetailDto.getLevel());

        return MemberDetailDto.toDto(member);
    }
}
