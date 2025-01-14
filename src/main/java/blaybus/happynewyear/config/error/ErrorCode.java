package blaybus.happynewyear.config.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@Getter
public enum ErrorCode {

    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "E401001", "사용자 인증에 실패했습니다."),
    INVALID_CURRENT_PASSWORD(HttpStatus.UNAUTHORIZED, "E401002", "잘못된 패스워드입니다."),

    DUPLICATED_USERNAME(HttpStatus.CONFLICT, "E409001", "이미 사용중인 사용자 이름입니다."),
    DUPLICATED_ID(HttpStatus.CONFLICT, "E409002", "이미 존재하는 사번입니다."),
    DUPLICATED_TEAM(HttpStatus.CONFLICT, "E409003", "이미 존재하는 팀입니다"),
    DUPLICATED_QUEST(HttpStatus.CONFLICT, "E409004", "이미 존재하는 퀘스트 타입입니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "E404001", "존재하지 않는 아이디입니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "E404002", "존재하지 않는 게시글입니다."),
    QUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "E404003", "존재하지 않는 퀘스트입니다."),
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "E404004", "존재하지 않는 소속입니다."),
    CALENDAR_NOT_FOUND(HttpStatus.NOT_FOUND, "E404005", "존재하지 않는 캘린더입니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"E500001","서버 처리 중 오류가 발생했습니다. 관리자에게 문의하세요."),
    NO_NOTIFICATION(HttpStatus.NOT_FOUND,"E404005","사용자 알림이 존재하지 않습니다."),
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND,"E404006","요청한 알림이 존재하지 않습니다."),
    INVALID_READ_STATE(HttpStatus.BAD_REQUEST, "E400001","이미 읽은 알람입니다."),

    SHEET_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "E500002", "Google Sheet 데이터를 업데이트하는 데 실패했습니다."),
    SHEET_READ_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "E500003", "Google Sheet 데이터를 읽어오는 데 실패했습니다."),
    SHEET_DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "E404007", "Google Sheet에서 요청한 데이터를 찾을 수 없습니다.");


  
    private final String message;
    private final String code;
    private final HttpStatus status;

    ErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
