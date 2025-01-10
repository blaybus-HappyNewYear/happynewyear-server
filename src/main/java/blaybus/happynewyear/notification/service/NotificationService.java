package blaybus.happynewyear.notification.service;

import blaybus.happynewyear.notification.dto.NotificationDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;


public interface NotificationService {
    // 사용자 별 알림 목록 조회
    List<NotificationDto> getNotifications(HttpServletRequest req);

    // 알림 생성
    // void createExpGainNotification(String username, int ext); // 경험치 기능 구현 이후 마저 구현
    void createPostNotification(String title);

    // 알림 읽음 상태 업데이트
    void updateRead(Long notificationId, Boolean isRead);
}
