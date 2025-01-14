package blaybus.happynewyear.exp.dto.request;

import blaybus.happynewyear.exp.entity.Exp;
import blaybus.happynewyear.exp.enums.ExpType;
import blaybus.happynewyear.member.entity.Member;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class LeaderQuestDto {
    private int month;
    private int week;
    private Long memberId;
    private String memberName;
    private String questName;
    private String achievement;
    private int exp;
    private String comments;

    public Exp toExp(Member member, LocalDate earnedDate) {
        return Exp.builder()
                .exp(this.exp)
                .member(member)
                .earnedDate(earnedDate)
                .type(ExpType.LEADER_QUEST.getName() + "(" + this.questName + ")")
                .comments(this.comments)
                .build();
    }

}
