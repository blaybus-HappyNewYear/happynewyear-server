package blaybus.happynewyear.connector.controller;

import blaybus.happynewyear.connector.service.ConnectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConnectorController {

    private final ConnectorService connectorService;

    @PostMapping("/import-members")
    public ResponseEntity<String> importMembers(@RequestParam String sheetName) {
        try {
            connectorService.importAndSignUpMembers(sheetName);
            return ResponseEntity.ok("Members 구글시트 데이터 연동 성공");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Members 데이터 연동 실패: " + e.getMessage());
        }
    }
}
