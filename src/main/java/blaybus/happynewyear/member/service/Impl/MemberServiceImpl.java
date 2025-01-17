package blaybus.happynewyear.member.service.Impl;

import blaybus.happynewyear.calendar.entity.MonthCalendar;
import blaybus.happynewyear.calendar.entity.WeekCalendar;
import blaybus.happynewyear.calendar.repository.MonthCalendarRepository;
import blaybus.happynewyear.calendar.repository.WeekCalendarRepository;
import blaybus.happynewyear.calendar.service.CalendarService;
import blaybus.happynewyear.config.error.ErrorCode;
import blaybus.happynewyear.config.error.exception.BusinessException;
import blaybus.happynewyear.connector.service.BaseSheetService;
import blaybus.happynewyear.member.dto.*;
import blaybus.happynewyear.member.entity.Member;
import blaybus.happynewyear.member.entity.Team;
import blaybus.happynewyear.member.jwt.JwtTokenProvider;
import blaybus.happynewyear.member.repository.MemberRepository;
import blaybus.happynewyear.member.repository.TeamRepository;
import blaybus.happynewyear.member.service.MemberService;
import blaybus.happynewyear.redis.RedisService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TeamRepository teamRepository;
    private final RedisService redisService;
    private final CalendarService calendarService;
    private final WeekCalendarRepository weekCalendarRepository;
    private final MonthCalendarRepository monthCalendarRepository;

    private final BaseSheetService baseSheetService;

    @Override
    @Transactional
    public JwtToken signIn(String username, String password) {

        // 1. username + password 기반으로 Authentication 객체 생성
        // 이 때 Authentication은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // 2. 실제 검증. authenticate()를 통해 요청된 Member에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailService에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 jwt token 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        // 4. redis에 refresh token 저장
        long refreshTokenExpirationMillis = jwtTokenProvider.getRefreshTokenExpirationMillis();
        redisService.setValues(authentication.getName(), jwtToken.getRefreshToken(), Duration.ofMillis(refreshTokenExpirationMillis));
        log.info("username? : {}", authentication.getName());

        return jwtToken;
    }

    @Override
    @Transactional
    public void signUp(SignUpDto signUpDto) {

        validateDuplicateMember(signUpDto.getId(), signUpDto.getUsername());


        Team team = teamRepository.findByTeamNameAndTeamNumber(signUpDto.getTeamName(), signUpDto.getTeamNumber())
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_NOT_FOUND));

      
        //password 암호화
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());
        List<String> roles = new ArrayList<>();
        roles.add("USER");  //USER 권한 부여
        Member member = memberRepository.save(signUpDto.toEntity(encodedPassword, team, roles));

        // 캘린더 생성
        createMemberCalendar(member);
    }

    public void validateDuplicateMember(Long id, String username) {

        // id 중복 검증
        Optional<Member> findById = memberRepository.findById(id);
        if (findById.isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATED_ID);
        }

        // username 중복 검증
        Optional<Member> findMembers = memberRepository.findByUsername(username);
        if(findMembers.isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATED_USERNAME);
        }
    }

    @Override
    public void signOut(String accessToken) {
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        String username = claims.getSubject();

        if (!redisService.getValues(username).equals("false")) {
            redisService.deleteValues(username);

            // 로그아웃시 Access Token 블랙리스트에 저장
            long accessTokenExpirationMillis = jwtTokenProvider.getAccessTokenExpirationMillis();
            redisService.setValues(accessToken, "logout", Duration.ofMillis(accessTokenExpirationMillis));
            log.info("logout success");
        }
    }

    @Override
    @Transactional
    public MemberInfoDto getMemberInfo(String accessToken) {
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        String username = claims.getSubject();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return MemberInfoDto.toDto(member);
    }

    @Override
    @Transactional
    public void updatePassword(String accessToken, PasswordUpdateDto passwordUpdateDto) {
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        String username = claims.getSubject();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(passwordUpdateDto.getOldPassword(), member.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CURRENT_PASSWORD);
        }

        String encodedPassword = passwordEncoder.encode(passwordUpdateDto.getNewPassword());
        member.setPassword(encodedPassword);
        memberRepository.save(member);

        // 구글 시트 데이터 반영
        String sheetName = "참고. 구성원 정보";
        String idColumn = "H";
        String passwordColumn = "J";
        String startRow = "10";

        try {
            baseSheetService.updatePasswordInSheet(sheetName, idColumn, passwordColumn, startRow, username, passwordUpdateDto.getNewPassword());
        } catch (Exception e) {
            throw new RuntimeException("구글 시트 업데이트 중 오류 발생", e);
        }
    }

    @Override
    @Transactional
    public void updateCharacter(String accessToken, int imgNumber) {
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        String username = claims.getSubject();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        member.setImgNumber(imgNumber);
        memberRepository.save(member);
    }

     private void createMemberCalendar(Member member) {
        int startYear = 2024;
        int endYear = 2026;
        for (int year = startYear; year <= endYear; year++) {
            List<WeekCalendar> calendarData = calendarService.generateWeeklyCalendar(year, member);
            List<MonthCalendar> monthCalendars = calendarService.generateMonthlyCalendar(year, member);
            weekCalendarRepository.saveAll(calendarData);
            monthCalendarRepository.saveAll(monthCalendars);
        }
    }

    /*구글 시트에서 변경사항을 저장하기 위해 필요한 서비스*/

    @Override
    @Transactional
    public void updateMember(MemberUpdateDto updateDto) {
        Member member = memberRepository.findByUsername(updateDto.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (updateDto.getName() != null) member.setName(updateDto.getName());
        if (updateDto.getStartDate() != null) member.setStartDate(updateDto.getStartDate());

        if (updateDto.getTeam() != null && updateDto.getTeamNumber() != null) {
            Team team = teamRepository.findByTeamNameAndTeamNumber(updateDto.getTeam(), updateDto.getTeamNumber())
                    .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_NOT_FOUND));
            member.setTeam(team);
        }

        if (updateDto.getJobGroup() != null) member.setJobGroup(updateDto.getJobGroup());
        if (updateDto.getLevel() != null) member.setLevel(updateDto.getLevel());

        // 패스워드 업데이트는 암호화 처리
        if (updateDto.getPassword() != null) {
            String encodedPassword = passwordEncoder.encode(updateDto.getPassword());
            member.setPassword(encodedPassword);
        }

        if (updateDto.getImgNumber() != null) member.setImgNumber(updateDto.getImgNumber());

        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void deleteMember(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        memberRepository.delete(member);
    }

    @Override
    @Transactional
    public MemberExpDto getMemberExp(String accessToken){
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        String username = claims.getSubject();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 시트 데이터 검색조건에 유저 id 넣어주기
        String sheetName = "예시. 경험치 현황";
        String rangeToWrite = "B11:B11"; // 경험치 현황시트의 사번 정보가 적혀있는 칸
        List<List<Object>> idData = List.of(List.of(member.getId()));
        try{
            baseSheetService.writeSheetData(sheetName, rangeToWrite, idData);
        }
        catch (Exception e) {
            throw new BusinessException(ErrorCode.SHEET_READ_FAILED);
        }

        // 유저 id로 세팅한 시트 데이터 값을 가져오기
        String rangeToRead = "C14:F14";
        try{
            List<List<Object>> readSheetData = baseSheetService.readSheetData(sheetName, rangeToRead);
            if (readSheetData == null || readSheetData.isEmpty() || readSheetData.get(0).size() < 4) throw new BusinessException(ErrorCode.SHEET_READ_FAILED);
            List<Object> rowData = readSheetData.get(0);
            int accumExp = Integer.parseInt(rowData.get(1).toString().replace(",",""));
            int currExp = Integer.parseInt(rowData.get(2).toString().replace(",",""));
            int necessaryExp = Integer.parseInt(rowData.get(3).toString().replace(",",""));
            int presentPercent = (int) ((double) accumExp / necessaryExp * 100);
            int currentPercent = currExp >= 9000 ? 100 : (int) ((double) currExp / 9000 * 100);

            return MemberExpDto.builder()
                    .id(member.getId())
                    .accumExp(accumExp)
                    .currExp(currExp)
                    .currlimit(9000)
                    .necessaryExp(necessaryExp)
                    .presentPercent(presentPercent)
                    .currentPercent(currentPercent)
                    .build();

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SHEET_READ_FAILED, "시트 데이터 읽기 실패: " + e.getMessage());
        }

    }

    // FCM 토큰 업데이트 메서드
    @Transactional
    public void updateFcmToken(String accessToken, String fcmToken) {
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        String username = claims.getSubject();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        member.setFcmToken(fcmToken); // FCM 토큰 설정
        log.info("FCMToken: {}", fcmToken);
        memberRepository.save(member);
    }


    @Override
    @Transactional
    public boolean memberExists(String username) {
        return memberRepository.existsByUsername(username);
    }



}
