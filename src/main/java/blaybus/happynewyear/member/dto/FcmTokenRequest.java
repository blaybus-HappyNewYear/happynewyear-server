package blaybus.happynewyear.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FcmTokenRequest {
    private String fcmToken;
}
