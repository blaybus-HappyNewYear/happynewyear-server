package blaybus.happynewyear.notification.service.impl;

import blaybus.happynewyear.config.error.ErrorCode;
import blaybus.happynewyear.config.error.exception.BusinessException;
import blaybus.happynewyear.member.entity.Member;
import blaybus.happynewyear.member.jwt.JwtTokenProvider;
import blaybus.happynewyear.member.repository.MemberRepository;
import blaybus.happynewyear.notification.dto.NotificationDto;
import blaybus.happynewyear.notification.entity.Notification;
import blaybus.happynewyear.notification.entity.NotificationType;
import blaybus.happynewyear.notification.repository.NotificationRepository;
import blaybus.happynewyear.notification.service.FCMService;
import blaybus.happynewyear.notification.service.NotificationService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.webauthn.management.UserCredentialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final FCMService fcmService;

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


    @Transactional
    public void createPostNotification(String title) {
        List<Member> members = memberRepository.findAll();

        String content = '"'+title +'"' + "라는 새로운 게시글이 등록되었어요.";

        for(Member member : members) {
            Notification notification = new Notification().builder()
                    .username(member.getUsername())
                    .type(NotificationType.POST_CREATE)
                    .content(content)
                    .isRead(false)
                    .timestamp(LocalDateTime.now().toString())
                    .build();

            notificationRepository.save(notification);
            if (member.getFcmToken() != null) {
                fcmService.sendNotification(member.getFcmToken(), "게시글", content);
            }
        }
    }

    @Transactional
    public void createLeaderNotification(Long memberId, String questName,int exp){
    Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        String content = "\"리더부여 퀘스트 " + questName + "\"를 완료하셨습니다. 경험치 " + exp + " do를 획득하셨습니다.";
        Notification notification = new Notification().builder()
                .username(member.getUsername())
                .type(NotificationType.EXP_GAIN)
                .content(content)
                .isRead(false)
                .timestamp(LocalDateTime.now().toString())
                .build();
        if (member.getFcmToken() != null) {
            fcmService.sendNotification(member.getFcmToken(), "경험치", content);
        }

        notificationRepository.save(notification);

    }

    @Transactional
    public void createTeamNotification(Long memberId, String comments,int exp){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        String content = "\"직무별 퀘스트에서 생산성 " + comments + "\"를 달성하셨습니다. 경험치 " + exp + " do를 획득하셨습니다.";
        Notification notification = new Notification().builder()
                .username(member.getUsername())
                .type(NotificationType.EXP_GAIN)
                .content(content)
                .isRead(false)
                .timestamp(LocalDateTime.now().toString())
                .build();

        if (member.getFcmToken() != null) {
            fcmService.sendNotification(member.getFcmToken(), "경험치", content);
        }

        notificationRepository.save(notification);

    }

    @Transactional
    public void createTfProjectNotification(Long memberId, String questName,int exp){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        String content = "\"전사프로젝트 " + questName + "\"를 완료하셨습니다. 경험치 " + exp + " do를 획득하셨습니다.";
        Notification notification = new Notification().builder()
                .username(member.getUsername())
                .type(NotificationType.EXP_GAIN)
                .content(content)
                .isRead(false)
                .timestamp(LocalDateTime.now().toString())
                .build();

        if (member.getFcmToken() != null) {
            fcmService.sendNotification(member.getFcmToken(), "경험치", content);
        }
        notificationRepository.save(notification);

    }

    @Transactional
    public void createPerfEvalNotification(Long memberId, String grade,int exp){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        String content = "\"인사평가 등급 " + grade + "\"입니다. 경험치 " + exp + " do를 획득하셨습니다.";
        Notification notification = new Notification().builder()
                .username(member.getUsername())
                .type(NotificationType.EXP_GAIN)
                .content(content)
                .isRead(false)
                .timestamp(LocalDateTime.now().toString())
                .build();

        if (member.getFcmToken() != null) {
            fcmService.sendNotification(member.getFcmToken(), "경험치", content);
        }
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
