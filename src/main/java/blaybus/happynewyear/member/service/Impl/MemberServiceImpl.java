package blaybus.happynewyear.member.service.Impl;

import blaybus.happynewyear.config.error.ErrorCode;
import blaybus.happynewyear.config.error.exception.BusinessException;
import blaybus.happynewyear.member.dto.JwtToken;
import blaybus.happynewyear.member.dto.MemberInfoDto;
import blaybus.happynewyear.member.dto.PasswordUpdateDto;
import blaybus.happynewyear.member.dto.SignUpDto;
import blaybus.happynewyear.member.entity.Member;
import blaybus.happynewyear.member.entity.Team;
import blaybus.happynewyear.member.jwt.JwtTokenProvider;
import blaybus.happynewyear.member.repository.MemberRepository;
import blaybus.happynewyear.member.repository.TeamRepository;
import blaybus.happynewyear.member.service.MemberService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public JwtToken signIn(String username, String password) {

        // 1. username + password 기반으로 Authentication 객체 생성
        // 이 때 Authentication은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // 2. 실제 검증. authenticate()를 통해 요청된 Member에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailService에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return jwtToken;
    }

    @Override
    @Transactional
    public void signUp(SignUpDto signUpDto) {
        validateDuplicateMember(signUpDto.getUsername());
        Team team = teamRepository.findByTeamNameAndTeamNumber(signUpDto.getTeamName(), signUpDto.getTeamNumber())
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_NOT_FOUND));
        //password 암호화
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());
        List<String> roles = new ArrayList<>();
        roles.add("USER");  //USER 권한 부여
        memberRepository.save(signUpDto.toEntity(encodedPassword, team, roles));
    }

    public void validateDuplicateMember(String username) {
        Optional<Member> findMembers = memberRepository.findByUsername(username);
        if(findMembers.isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATED_USERNAME);
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
}
