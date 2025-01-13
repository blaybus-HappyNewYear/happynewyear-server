package blaybus.happynewyear.exp.dto.request;

import blaybus.happynewyear.exp.entity.Exp;
import blaybus.happynewyear.exp.entity.TeamExp;
import blaybus.happynewyear.exp.enums.ExpType;
import blaybus.happynewyear.member.entity.Member;
import blaybus.happynewyear.member.entity.Team;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class TeamQuestDto {
    private String teamName;
    private int teamNumber;
    private String cycle;
    private int monthOrWeek;
    private int exp;
    private String comments;

    public TeamExp toTeamExp(Team team, LocalDate earnedDate) {
        return TeamExp.builder()
                .team(team)
                .exp(this.exp)
                .cycle(this.cycle)
                .monthOrWeek(this.monthOrWeek)
                .earnedDate(earnedDate)
                .type(ExpType.TEAM_QUEST.getName())
                .build();
    }

    public Exp toExp(Member member, LocalDate earnedDate) {
        return Exp.builder()
                .type(ExpType.TEAM_QUEST.getName())
                .member(member)
                .exp(this.exp)
                .earnedDate(earnedDate)
                .comments(this.comments)
                .build();
    }
}
