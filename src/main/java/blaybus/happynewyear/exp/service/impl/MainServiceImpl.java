package blaybus.happynewyear.exp.service.impl;

import blaybus.happynewyear.config.error.ErrorCode;
import blaybus.happynewyear.config.error.exception.BusinessException;
import blaybus.happynewyear.exp.dto.reponse.AllExpDto;
import blaybus.happynewyear.exp.dto.reponse.RecentExpDto;
import blaybus.happynewyear.exp.entity.Exp;
import blaybus.happynewyear.exp.repository.ExpRepository;
import blaybus.happynewyear.exp.service.MainService;
import blaybus.happynewyear.member.dto.CharacterDto;
import blaybus.happynewyear.member.entity.Member;
import blaybus.happynewyear.member.jwt.JwtTokenProvider;
import blaybus.happynewyear.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class MainServiceImpl implements MainService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final ExpRepository expRepository;

    @Override
    @Transactional
    public List<RecentExpDto> getRecentExp(String accessToken) {
        // access token으로부터 member 파싱
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        String username = claims.getSubject();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 해당 멤버의 최근 다섯개의 경험치 리턴
        return expRepository.findTop5ByMemberOrderByIdDesc(member).stream()
                .map(RecentExpDto::expToDto)
                .toList();
    }

    @Override
    @Transactional
    public Page<AllExpDto> getAllExp(String accessToken, Pageable pageable) {
        int page = pageable.getPageNumber() - 1;    // page 위치에 있는 값은 0부터 시작
        int pageLimit = 12;  // 한 페이지에 보여줄 글 개수

        // access token으로부터 member 파싱
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        String username = claims.getSubject();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 페이징 정보를 기반으로 Exp 데이터를 조회
        PageRequest pageRequest = PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id"));
        Page<Exp> expPage = expRepository.findByMember(member, pageRequest);

        // Exp 데이터를 AllExpDto로 변환
        return expPage.map(AllExpDto::expToDto);
    }

    @Override
    @Transactional
    public CharacterDto getCharacter(String accessToken) {
        // access token으로부터 member 파싱
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        String username = claims.getSubject();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return CharacterDto.toDto(member.getImgNumber());
    }
}
