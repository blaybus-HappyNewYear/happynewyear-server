package blaybus.happynewyear.connector.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class BaseSheetService {
    @Value("${google.sheets.spreadsheet-id}")
    private String spreadsheetId;

    @Value("${google.credentials.file}")
    private Resource credentialsResource;

    private Sheets getSheetsService() throws Exception {
        InputStream credentialsStream = credentialsResource.getInputStream();
        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream)
                .createScoped(List.of("https://www.googleapis.com/auth/spreadsheets"));

        return new Sheets.Builder(
                new com.google.api.client.http.javanet.NetHttpTransport(),
                com.google.api.client.json.jackson2.JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName("Google Sheets Integration")
                .build();
    }

    // 시트 데이터 읽어오는 작업
    public List<List<Object>> readSheetData(String sheetName, String range) throws Exception {
        Sheets sheetsService = getSheetsService();
        String fullRange = sheetName + "!" + range;

        ValueRange response = sheetsService.spreadsheets().values()
                .get(spreadsheetId, fullRange)
                .execute();

        return response.getValues();
    }

    // 시트에 데이터를 쓰는 작업
    public void writeSheetData(String sheetName, String range, List<List<Object>> data) throws Exception {
        Sheets sheetsService = getSheetsService();
        String fullRange = sheetName + "!" + range;

        ValueRange body = new ValueRange().setValues(data);

        sheetsService.spreadsheets().values()
                .update(spreadsheetId, fullRange, body)
                .setValueInputOption("RAW")
                .execute();
    }

    public void updatePasswordInSheet(String sheetName, String idColumn, String passwordColumn, String startRow, String memberId, String newPassword) throws Exception {
        Sheets sheetsService = getSheetsService();

        // 아이디 범위를 설정 (H열에서 10행부터 탐색)
        String idRange = sheetName + "!" + idColumn + startRow + ":" + idColumn;
        ValueRange idResponse = sheetsService.spreadsheets().values()
                .get(spreadsheetId, idRange)
                .execute();

        List<List<Object>> idValues = idResponse.getValues();

        if (idValues == null || idValues.isEmpty()) {
            throw new IllegalArgumentException("시트에 데이터가 없습니다.");
        }

        // 아이디 탐색
        int rowIndex = -1; // 아이디가 있는 행 인덱스
        for (int i = 0; i < idValues.size(); i++) {
            if (idValues.get(i).size() > 0 && idValues.get(i).get(0).equals(memberId)) {
                rowIndex = i + Integer.parseInt(startRow); // 실제 행 번호 계산
                break;
            }
        }

        if (rowIndex == -1) {
            throw new IllegalArgumentException("해당 아이디를 시트에서 찾을 수 없습니다.");
        }

        // 비밀번호 열(J열)에 새 비밀번호 채우기
        String passwordRange = sheetName + "!" + passwordColumn + rowIndex + ":" + passwordColumn + rowIndex;

        ValueRange body = new ValueRange().setValues(List.of(List.of(newPassword))); // 새 비밀번호 값 설정
        sheetsService.spreadsheets().values()
                .update(spreadsheetId, passwordRange, body)
                .setValueInputOption("RAW")
                .execute();
    }
}