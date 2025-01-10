package blaybus.happynewyear.notification.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class NotificationListDto {
    private int totalNotifications; // 전체 알림 수
    private List<NotificationDto> notifications; // 알림 목록

}
