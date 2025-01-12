package blaybus.happynewyear.exp.dto.request;

import blaybus.happynewyear.exp.entity.Exp;
import blaybus.happynewyear.exp.enums.ExpType;
import blaybus.happynewyear.member.entity.Member;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class TfProjectDto {
    private int month;
    private int day;
    private Long memberId;
    private String memberName;
    private String projectName;
    private int exp;
    private String comments;

    public Exp toEntity(Member member, LocalDate earnedDate) {
        return Exp.builder()
                .exp(this.exp)
                .member(member)
                .earnedDate(earnedDate)
                .type(ExpType.TF_PROJECT.getName() + "(" + this.projectName + ")")
                .comments(this.comments)
                .build();
    }
}
