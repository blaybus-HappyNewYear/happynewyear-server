package blaybus.happynewyear.notification.service;

import blaybus.happynewyear.config.error.ErrorCode;
import blaybus.happynewyear.config.error.exception.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static blaybus.happynewyear.config.error.ErrorCode.ACCESS_TOKEN_GENERATION_FAILED;

@Service
@Slf4j
public class FCMService {

    @Value("${fcm.api-url}")
    private String fcmApiUrl;

    @Value("${fcm.credentials-file}")
    private String credentialsFile;

    // Access Token 생성 메서드
    private String getAccessToken() {
        try {
            // credentials-file 경로에서 서비스 계정 키 파일 로드
            log.info("키 파일 로드");
            InputStream serviceAccountStream =
                    getClass().getClassLoader().getResourceAsStream(credentialsFile);

            log.info(serviceAccountStream.toString());
            if (serviceAccountStream == null) {
                throw new IllegalArgumentException("서비스 계정 키 파일을 찾을 수 없습니다: " + credentialsFile);
            }

            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(serviceAccountStream)
                    .createScoped(Collections.singletonList("https://www.googleapis.com/auth/firebase.messaging"));
            googleCredentials.refreshIfExpired();

            log.info("어디가 문제냐 .. ");
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (Exception e) {
            // 비즈니스 예외로 변경
            throw new BusinessException(ErrorCode.ACCESS_TOKEN_GENERATION_FAILED);
        }
    }

    // FCM 알림 전송 메서드
    public void sendNotification(String fcmToken, String title, String body) {
        try {
            log.info(fcmToken);
            String accessToken = getAccessToken();

            RestTemplate restTemplate = new RestTemplate();

            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken); // OAuth 2.0 액세스 토큰 설정
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 메시지 요청 JSON 생성
            ObjectMapper objectMapper = new ObjectMapper();

            Map<String, Object> notification = new HashMap<>();
            notification.put("title", title);
            notification.put("body", body);

            Map<String, Object> message = new HashMap<>();
            message.put("token", fcmToken);
            message.put("notification", notification);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("message", message);

            String messageJson = objectMapper.writeValueAsString(requestBody);

            // 요청 생성 및 전송
            HttpEntity<String> request = new HttpEntity<>(messageJson, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    fcmApiUrl,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            log.info("FCM Response: {}", response.getBody());
        } catch (Exception e) {
            log.error("알림 전송 중 오류 발생: ", e);
        }
    }
}
