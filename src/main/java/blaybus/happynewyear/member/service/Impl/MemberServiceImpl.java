package blaybus.happynewyear.member.service.Impl;

import blaybus.happynewyear.member.dto.JwtToken;
import blaybus.happynewyear.member.dto.SignUpDto;
import blaybus.happynewyear.member.entity.Member;
import blaybus.happynewyear.member.jwt.JwtTokenProvider;
import blaybus.happynewyear.member.repository.MemberRepository;
import blaybus.happynewyear.member.service.MemberService;
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
        //password 암호화
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());
        List<String> roles = new ArrayList<>();
        roles.add("USER");  //USER 권한 부여
        memberRepository.save(signUpDto.toEntity(encodedPassword, roles));
    }

    public void validateDuplicateMember(String username) {
        Optional<Member> findMembers = memberRepository.findByUsername(username);
        if(findMembers.isPresent()) {
            throw new IllegalStateException("이미 사용 중인 사용자 이름입니다.");
        }
    }
}
