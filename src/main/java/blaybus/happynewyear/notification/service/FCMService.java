package blaybus.happynewyear.notification.service;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.util.Collections;

@Service
public class FCMService {

    @Value("${fcm.api-url}")
    private String fcmApiUrl;

    @Value("${fcm.credentials-file}")
    private String credentialsFile;

    // Access Token 생성 메서드
    private String getAccessToken() {
        try {
            // credentials-file 경로에서 서비스 계정 키 파일 로드
            InputStream serviceAccountStream =
                    getClass().getClassLoader().getResourceAsStream(credentialsFile);

            if (serviceAccountStream == null) {
                throw new IllegalArgumentException("서비스 계정 키 파일을 찾을 수 없습니다: " + credentialsFile);
            }

            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(serviceAccountStream)
                    .createScoped(Collections.singletonList("https://www.googleapis.com/auth/firebase.messaging"));
            googleCredentials.refreshIfExpired();

            return googleCredentials.getAccessToken().getTokenValue();
        } catch (Exception e) {
            throw new RuntimeException("Access Token 생성 중 오류 발생", e);
        }
    }

    // FCM 알림 전송 메서드
    public void sendNotification(String fcmToken, String title, String body) {
        try {
            String accessToken = getAccessToken();

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken); // OAuth 2.0 액세스 토큰 설정
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 메시지 요청 JSON 구성
            String message = "{"
                    + "\"message\":{"
                    + "\"token\":\"" + fcmToken + "\","
                    + "\"notification\":{"
                    + "\"title\":\"" + title + "\","
                    + "\"body\":\"" + body + "\""
                    + "}"
                    + "}"
                    + "}";

            // 요청 생성 및 전송
            HttpEntity<String> request = new HttpEntity<>(message, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    fcmApiUrl,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            System.out.println("FCM Response: " + response.getBody());
        } catch (Exception e) {
            System.err.println("알림 전송 중 오류 발생: " + e.getMessage());
        }
    }
}
