package blaybus.happynewyear.notification.service.impl;

import blaybus.happynewyear.config.error.ErrorCode;
import blaybus.happynewyear.config.error.exception.BusinessException;
import blaybus.happynewyear.member.jwt.JwtTokenProvider;
import blaybus.happynewyear.notification.dto.NotificationDto;
import blaybus.happynewyear.notification.entity.Notification;
import blaybus.happynewyear.notification.entity.NotificationType;
import blaybus.happynewyear.notification.repository.NotificationRepository;
import blaybus.happynewyear.notification.service.NotificationService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final JwtTokenProvider jwtTokenProvider;
/*
    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> getNotifications(String accessToken) {
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        List<Notification> notifications = notificationRepository.findByUsername(username);
        if (notifications.isEmpty()) {
            return Collections.emptyList();
        }

        return notifications.stream()
                .map(notification -> NotificationDto.builder()
                        .id(notification.getId())
                        .type(notification.getType().name())
                        .content(notification.getContent())
                        .isRead(notification.isRead())
                        .timestamp(notification.getTimestamp())
                        .build())
                .collect(Collectors.toList());
    }

 */
public List<NotificationDto> getNotifications(HttpServletRequest request) {
    // 1. Access Token 추출
    String accessToken = jwtTokenProvider.resloveAccessToken(request);
    if (accessToken == null) {
        throw new IllegalArgumentException("Authorization 헤더가 없거나 형식이 잘못되었습니다.");
    }

    // 2. JWT 토큰에서 사용자 정보 추출
    Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String username = userDetails.getUsername();

    // 3. 사용자 알림 조회
    List<Notification> notifications = notificationRepository.findByUsername(username);

    if (notifications.isEmpty()) {
        return Collections.emptyList();
    }

    // 4. DTO 변환
    return notifications.stream()
            .map(notification -> NotificationDto.builder()
                    .id(notification.getId())
                    .type(notification.getType().name())
                    .content(notification.getContent())
                    .isRead(notification.isRead())
                    .timestamp(notification.getTimestamp())
                    .build())
            .collect(Collectors.toList());
}

    @Override
    @Transactional
    public void createNotification(String username, String type, String content) {
        Notification notification = Notification.builder()
                .username(username)
                .type(NotificationType.valueOf(type))
                .content(content)
                .isRead(false)
                .timestamp(LocalDateTime.now().toString())
                .build();

        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void updateRead(Long notificationId, Boolean isRead) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOTIFICATION_NOT_FOUND));

        if (notification.isRead() == isRead) {
            throw new BusinessException(ErrorCode.INVALID_READ_STATE);
        }

        notification.setRead(isRead);
        notificationRepository.save(notification);
    }
}
