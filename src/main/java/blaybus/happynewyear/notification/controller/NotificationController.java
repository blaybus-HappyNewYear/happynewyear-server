package blaybus.happynewyear.notification.controller;

import blaybus.happynewyear.notification.dto.NotificationDto;
import blaybus.happynewyear.notification.dto.NotificationReadDto;
import blaybus.happynewyear.notification.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/notification")
    public ResponseEntity<List<NotificationDto>> getNotifications(HttpServletRequest request) {
        List<NotificationDto> notifications = notificationService.getNotifications(request);
        return ResponseEntity.ok(notifications);
    }

    @PatchMapping("notification/{id}")
    public ResponseEntity<String> updateRead(
            @PathVariable Long id,
            @RequestBody NotificationReadDto notificationReadDto) {

        if (notificationReadDto.getIsRead() == null) {
            return ResponseEntity.badRequest().body("읽음 상태(isRead)를 제공해야 합니다.");
        }

        notificationService.updateRead(id, notificationReadDto.getIsRead());

        return ResponseEntity.ok("알림 읽음 표시 처리 성공");
    }
}
