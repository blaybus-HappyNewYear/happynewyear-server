package blaybus.happynewyear.connector.service;

import blaybus.happynewyear.connector.GoogleSheetsConnector;
import blaybus.happynewyear.member.dto.SignUpDto;
import blaybus.happynewyear.member.entity.Member;
import blaybus.happynewyear.member.repository.MemberRepository;
import blaybus.happynewyear.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ConnectorService {

    private final GoogleSheetsConnector googleSheetsConnector;
    private final MemberService memberService;

    public void importAndSignUpMembers(String sheetName) throws Exception {
        // Google Sheets에서 데이터 읽어오는 함수 호출
        List<List<Object>> sheetData = googleSheetsConnector.readDataFromSheets(sheetName);

        // 회원가입
        for (List<Object> row : sheetData) {
            if (row.size() < 2) continue; // 데이터가 부족하면 스킵

            String username = String.valueOf(row.get(0)); // 첫 번째 열은 username
            String password = String.valueOf(row.get(1)); // 두 번째 열은 password

            SignUpDto signUpDto = SignUpDto.builder()
                    .username(username)
                    .password(password)
                    .build();

            memberService.signUp(signUpDto);
        }
    }
}