package blaybus.happynewyear.exp.controller;

import blaybus.happynewyear.exp.dto.reponse.ExpTrendDto;
import blaybus.happynewyear.exp.service.ExpInfoService;
import blaybus.happynewyear.member.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExpInfoController {

    private final ExpInfoService expInfoService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/trend")
    public ResponseEntity<ExpTrendDto> getExpTrend(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        ExpTrendDto expTrendDto = expInfoService.getExpTrend(accessToken);
        return ResponseEntity.ok(expTrendDto);

    }
}
