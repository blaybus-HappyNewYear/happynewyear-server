package blaybus.happynewyear.exp.dto.request;

import blaybus.happynewyear.exp.entity.Exp;
import blaybus.happynewyear.exp.enums.ExpType;
import blaybus.happynewyear.member.entity.Member;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class PerfEvalDto {
    private Long memberId;
    private String memberName;
    private String grade;
    private int exp;
    private String comments;

    public Exp toEntity(Member member, LocalDate earnedDate) {
        return Exp.builder()
                .exp(this.exp)
                .member(member)
                .earnedDate(earnedDate)
                .type(ExpType.PERFORMANCE_EVALUATION.getName())
                .comments(grade)
                .build();
    }
}
