package blaybus.happynewyear.exp.service.impl;

import blaybus.happynewyear.config.error.ErrorCode;
import blaybus.happynewyear.config.error.exception.BusinessException;
import blaybus.happynewyear.connector.service.BaseSheetService;
import blaybus.happynewyear.exp.dto.reponse.ExpTrendDto;
import blaybus.happynewyear.exp.service.ExpInfoService;
import blaybus.happynewyear.member.entity.Member;
import blaybus.happynewyear.member.jwt.JwtTokenProvider;
import blaybus.happynewyear.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpInfoServiceImpl implements ExpInfoService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final BaseSheetService baseSheetService;

    @Override
    @Transactional
    public ExpTrendDto getExpTrend(String accessToken) {
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        String username = claims.getSubject();

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String sheetName = "참고. 구성원 정보";
        String rangeToReadLegend = "L9:W9"; // 범례 데이터
        String rangeToSearchIds = "B10:B100"; // B열의 10행~100행 데이터

        try {
            // 1. 범례 데이터 읽기
            List<List<Object>> legendData = baseSheetService.readSheetData(sheetName, rangeToReadLegend);
            if (legendData == null || legendData.isEmpty()) {
                throw new BusinessException(ErrorCode.SHEET_READ_FAILED, "범례 데이터 없음");
            }

            // "년"을 제거하고 정수로 변환
            List<Integer> legendRow = new ArrayList<>();
            for (Object legend : legendData.get(0)) {
                String legendStr = legend.toString();
                if (legendStr.endsWith("년")) {
                    legendStr = legendStr.substring(0, legendStr.length() - 1); // "년" 제거
                }
                int year = Integer.parseInt(legendStr); // 정수로 변환
                legendRow.add(year);
            }

            // 2. B열의 유저 아이디 데이터 읽기
            List<List<Object>> idData = baseSheetService.readSheetData(sheetName, rangeToSearchIds);
            if (idData == null || idData.isEmpty()) {
                throw new BusinessException(ErrorCode.SHEET_READ_FAILED, "사번 데이터 없음");
            }

            // 3. 유저 ID가 있는 행 찾기
            int targetRow = -1;
            for (int i = 0; i < idData.size(); i++) {
                List<Object> row = idData.get(i);
                if (!row.isEmpty() && row.get(0).toString().equals(member.getId().toString())) {
                    targetRow = 10 + i; // B10부터 시작했으므로 10을 더해줌
                    break;
                }
            }

            if (targetRow == -1) {
                throw new BusinessException(ErrorCode.USER_NOT_FOUND, "해당 사번을 찾을 수 없습니다.");
            }

            // 4. 찾은 행의 L열~W열 데이터 읽기
            String rangeToReadUser = String.format("L%d:W%d", targetRow, targetRow);
            List<List<Object>> userData = baseSheetService.readSheetData(sheetName, rangeToReadUser);

            List<Object> userRow = new ArrayList<>();
            if (userData == null || userData.isEmpty()) {
                for (int i = 0; i < legendRow.size(); i++) {
                    userRow.add(0);
                }
            } else {
                List<Object> rawData = userData.get(0);
                for (int i = 0; i < legendRow.size(); i++) {
                    if (i < rawData.size() && rawData.get(i) != null) {
                        String valueStr = rawData.get(i).toString();
                        // 쉼표 제거 후 숫자로 변환
                        int value = Integer.parseInt(valueStr.replace(",", ""));
                        userRow.add(value);
                    } else {
                        userRow.add(0); // 비어있는 데이터는 0으로 처리
                    }
                }
            }

            // 5. 범례와 데이터를 매핑하여 Map 생성
            Map<Integer, Integer> expData = new HashMap<>();
            for (int i = 0; i < legendRow.size(); i++) {
                int legend = legendRow.get(i); // 정수로 변환된 범례 데이터
                int value = (int) userRow.get(i); // 이미 숫자로 변환된 값
                expData.put(legend, value);
            }

            return ExpTrendDto.builder()
                    .expData(expData)
                    .build();

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SHEET_READ_FAILED, "시트 데이터 읽기 실패: " + e.getMessage());
        }
    }

}
