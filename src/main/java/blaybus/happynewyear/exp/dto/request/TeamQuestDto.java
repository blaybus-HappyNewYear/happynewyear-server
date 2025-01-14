package blaybus.happynewyear.exp.dto.request;

import blaybus.happynewyear.calendar.entity.MonthCalendar;
import blaybus.happynewyear.calendar.entity.Quest;
import blaybus.happynewyear.calendar.entity.WeekCalendar;
import blaybus.happynewyear.exp.entity.Exp;
import blaybus.happynewyear.exp.enums.ExpType;
import blaybus.happynewyear.member.entity.Member;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TeamQuestDto {
    private String teamName;
    private int teamNumber;
    private String cycle;
    private int monthOrWeek;
    private int exp;
    private String comments;
    private String achievement;

    public Exp toExp(Member member, LocalDate earnedDate) {
        return Exp.builder()
                .type(ExpType.TEAM_QUEST.getName())
                .member(member)
                .exp(this.exp)
                .earnedDate(earnedDate)
                .comments("생산성 " + this.comments)
                .build();
    }

    public Quest toQuest(MonthCalendar monthCalendar) {
        return Quest.builder()
                .questName(ExpType.TEAM_QUEST.getName())
                .comments("생산성 " + this.comments)
                .cycle(this.cycle)
                .monthCalendar(monthCalendar)
                .achievement(this.achievement)
                .exp(this.exp)
                .build();
    }

    public Quest toQuest(WeekCalendar weekCalendar, MonthCalendar monthCalendar) {
        return Quest.builder()
                .questName(ExpType.TEAM_QUEST.getName())
                .comments("생산성 " + this.comments)
                .cycle(this.cycle)
                .weekCalendar(weekCalendar)
                .monthCalendar(monthCalendar)
                .achievement(this.achievement)
                .exp(this.exp)
                .build();
    }
}
