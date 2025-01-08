package blaybus.happynewyear.connector;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class GoogleSheetsConnector {
    private static final String APPLICATION_NAME = "Google Sheets Integration";

    @Value("${google.sheets.spreadsheet-id}")
    private String spreadsheetId;

    @Value("${google.credentials.file}")
    private Resource credentialsResource;

    public List<List<Object>> readDataFromSheets(String sheetName) throws Exception {
        try (InputStream credentialsStream = credentialsResource.getInputStream()) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream)
                    .createScoped(List.of("https://www.googleapis.com/auth/spreadsheets.readonly"));

            Sheets sheets = new Sheets.Builder(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(credentials))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            String range = sheetName + "!B9:Z"; // 멤버는 B9부터 유의미한 데이터가 들어있음
            ValueRange response = sheets.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();

            return response.getValues();
        }
    }
}
