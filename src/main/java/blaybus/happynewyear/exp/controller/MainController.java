package blaybus.happynewyear.exp.controller;

import blaybus.happynewyear.exp.dto.reponse.AllExpDto;
import blaybus.happynewyear.exp.dto.reponse.CurrExpDto;
import blaybus.happynewyear.exp.dto.reponse.RecentExpDto;
import blaybus.happynewyear.exp.service.MainService;
import blaybus.happynewyear.member.dto.CharacterDto;
import blaybus.happynewyear.member.jwt.JwtTokenProvider;
import blaybus.happynewyear.post.dto.PostPreviewDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;
    private final JwtTokenProvider jwtTokenProvider;

    // 사용자가 최근에 획득한 경험치 5개
    @GetMapping("/main/recent-exp")
    public ResponseEntity<List<RecentExpDto>> getRecentExp(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        List<RecentExpDto> recentExpDtos = mainService.getRecentExp(accessToken);
        return ResponseEntity.ok(recentExpDtos);
    }



    // 사용자가 올해 획득한 경험치
    @PostMapping("/main/all-exp")
    public ResponseEntity<Map<String, Object>> getAllExp(HttpServletRequest request, @PageableDefault(page = 1) Pageable pageable) {
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        Page<AllExpDto> allExpDtos = mainService.getAllExp(accessToken, pageable);

        Map<String, Object> response = Map.of(
                "expList", allExpDtos.getContent(),       // 경험치 리스트
                "hasNext", allExpDtos.hasNext(),         // 다음 페이지 여부
                "totalElements", allExpDtos.getTotalElements() // 전체 요소 수 (선택 사항)
        );

        return ResponseEntity.ok(response);
    }

    // 사용자가 올해 획득한 경험치 양
    @GetMapping("/main/curr-exp")
    public ResponseEntity<CurrExpDto> getCurrExp(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        CurrExpDto currExpDto = mainService.getCurrExp(accessToken);
        return ResponseEntity.ok(currExpDto);
    }

    // 사용자 캐릭터
    @GetMapping("/main/character")
    public ResponseEntity<CharacterDto> getCharcter(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        CharacterDto characterDto = mainService.getCharacter(accessToken);
        return ResponseEntity.ok(characterDto);
    }

}
