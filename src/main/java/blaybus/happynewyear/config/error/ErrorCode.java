package blaybus.happynewyear.config.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "E404001", "존재하지 않는 아이디입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "E401001", "사용자 인증에 실패했습니다."),
    DUPLICATED_USERNAME(HttpStatus.CONFLICT, "E409001", "이미 사용중인 사용자 이름입니다."),
    DUPLICATED_ID(HttpStatus.CONFLICT, "E409002", "이미 존재하는 사번입니다."),
    INVALID_CURRENT_PASSWORD(HttpStatus.UNAUTHORIZED, "E401002", "잘못된 패스워드입니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "E404002", "존재하지 않는 게시글입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"E500001","서버 처리 중 오류가 발생했습니다. 관리자에게 문의하세요."),
    NO_NOTIFICATION(HttpStatus.NOT_FOUND,"E404003","사용자 알림이 존재하지 않습니다."),
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND,"E404004","요청한 알림이 존재하지 않습니다."),
    INVALID_READ_STATE(HttpStatus.BAD_REQUEST, "E400001","이미 읽은 알람입니다.");

    private final String message;
    private final String code;
    private final HttpStatus status;

    ErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
