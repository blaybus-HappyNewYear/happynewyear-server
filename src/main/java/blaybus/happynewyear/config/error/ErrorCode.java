package blaybus.happynewyear.config.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    ERROR_EXAMPLE(HttpStatus.NOT_FOUND, "E404001", "에러 예시"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"E500001","서버 처리 중 오류가 발생했습니다. 관리자에게 문의하세요.");

    private final String message;
    private final String code;
    private final HttpStatus status;

    ErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
