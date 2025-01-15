package blaybus.happynewyear.admin.service.impl;

import blaybus.happynewyear.admin.dto.MemberDetailDto;
import blaybus.happynewyear.admin.dto.MemberListDto;
import blaybus.happynewyear.admin.service.AdminService;
import blaybus.happynewyear.config.error.ErrorCode;
import blaybus.happynewyear.config.error.exception.BusinessException;
import blaybus.happynewyear.connector.service.BaseSheetService;
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
    private final BaseSheetService baseSheetService;

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

        //앱에서 글 생성 시, 해당 데이터를 시트에도 보내야 함.
        String sheetName = "참고. 게시판";
        String columnToSearch = "B";
        int startRow =7;

        try {
            // B열 7행부터 아래로 탐색하며 비어있는 셀 찾기
            int emptyRow = startRow;
            String rangeToRead;
            List<List<Object>> readSheetData;
            String lastValue = null; // 비어있는 셀 직전 데이터

            while (true) {
                rangeToRead = columnToSearch + emptyRow + ":" + columnToSearch + emptyRow;
                readSheetData = baseSheetService.readSheetData(sheetName, rangeToRead);

                if (readSheetData == null || readSheetData.isEmpty() || readSheetData.get(0).isEmpty()) {
                    // 비어있는 셀 발견 시 루프 종료
                    break;
                }

                // 비어있지 않으면 데이터를 갱신
                lastValue = readSheetData.get(0).get(0).toString();
                emptyRow++;
            }

            // 마지막 데이터가 없으면 초기값으로 설정
            int nextNumber = (lastValue != null) ? Integer.parseInt(lastValue) + 1 : 1;

            // 데이터 작성 범위 설정
            String rangeToWrite = "B" + emptyRow + ":D" + emptyRow;
            List<List<Object>> dataToWrite = List.of(
                    List.of(nextNumber, createPostDto.getTitle(), createPostDto.getContent())
            );

            // 데이터 작성
            baseSheetService.writeSheetData(sheetName, rangeToWrite, dataToWrite);

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SHEET_UPDATE_FAILED, "시트 데이터 작성 실패: " + e.getMessage());
        }


    }

    @Override
    @Transactional
    public void createMember(SignUpDto signUpDto) {
        // 멤버 등록
        memberService.signUp(signUpDto);

        String sheetName = "참고. 구성원 정보";
        String columnToSearch = "B";
        int startRow = 10;

        try {
            int emptyRow = startRow;
            String rangeToRead;
            List<List<Object>> readSheetData;

            while (true) {
                rangeToRead = columnToSearch + emptyRow + ":" + columnToSearch + emptyRow;
                readSheetData = baseSheetService.readSheetData(sheetName, rangeToRead);

                if (readSheetData == null || readSheetData.isEmpty() || readSheetData.get(0).isEmpty()) {
                    break;
                }
                emptyRow++;
            }

            // 데이터 작성 범위 설정
            String rangeToWrite = "B" + emptyRow + ":I" + emptyRow; // B열부터 I열까지
            List<List<Object>> dataToWrite = List.of(
                    List.of(
                            signUpDto.getId(),
                            signUpDto.getName(),
                            signUpDto.getStartDate(),
                            signUpDto.getTeamName(),
                            signUpDto.getTeamNumber(),
                            signUpDto.getLevel(),
                            signUpDto.getUsername(),
                            signUpDto.getPassword()
                    )
            );

            baseSheetService.writeSheetData(sheetName, rangeToWrite, dataToWrite);

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SHEET_UPDATE_FAILED, "시트 데이터 작성 실패: " + e.getMessage());
        }
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

        String sheetName = "참고. 구성원 정보";
        String columnToSearch = "B";
        int startRow = 10;

        try {
            int targetRow = -1;
            String rangeToRead;
            List<List<Object>> readSheetData;

            for (int currentRow = startRow; ; currentRow++) {
                rangeToRead = columnToSearch + currentRow + ":" + columnToSearch + currentRow;
                readSheetData = baseSheetService.readSheetData(sheetName, rangeToRead);

                if (readSheetData == null || readSheetData.isEmpty() || readSheetData.get(0).isEmpty()) {
                    break;
                }

                String cellValue = readSheetData.get(0).get(0).toString();
                if (cellValue.equals(String.valueOf(memberId))) {
                    targetRow = currentRow;
                    break;
                }
            }

            if (targetRow == -1) {
                throw new BusinessException(ErrorCode.USER_NOT_FOUND, "시트에서 해당 유저 ID를 찾을 수 없습니다.");
            }

            // 데이터 작성 범위 설정
            String rangeToWrite = "B" + targetRow + ":I" + targetRow;
            List<List<Object>> dataToWrite = List.of(
                    List.of(
                            memberId,
                            memberDetailDto.getName(),
                            memberDetailDto.getStartDate(),
                            memberDetailDto.getTeamName(),
                            memberDetailDto.getTeamNumber(),
                            memberDetailDto.getLevel(),
                            member.getUsername(),
                            member.getPassword()
                    )
            );

            // 데이터 작성
            baseSheetService.writeSheetData(sheetName, rangeToWrite, dataToWrite);

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SHEET_UPDATE_FAILED, "시트 데이터 업데이트 실패: " + e.getMessage());
        }

        return MemberDetailDto.toDto(member);
    }
}
