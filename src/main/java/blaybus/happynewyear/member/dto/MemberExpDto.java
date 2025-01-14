package blaybus.happynewyear.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberExpDto {
    private Long id;
    private int accumExp; //작년까지 누적 경험치
    private int currExp; //올해 경험치
    private int necessaryExp; // 다음 레벨까지 남은 경험치
    private int presentPercent; // 누적 경험치 바 퍼센테이지
    private int currentPercent; //올해 경험치 바 퍼센테이지
    private int currlimit; //올해 얻을 수 있는 경험치 상한 값

}
