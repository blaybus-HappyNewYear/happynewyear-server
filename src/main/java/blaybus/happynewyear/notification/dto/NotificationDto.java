package blaybus.happynewyear.notification.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationDto {
    private Long id;
    private String type;
    private String content;
    private boolean isRead;
    private String timestamp;
}
