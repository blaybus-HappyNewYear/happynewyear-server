package blaybus.happynewyear.exp.dto.request;

import blaybus.happynewyear.calendar.entity.MonthCalendar;
import blaybus.happynewyear.calendar.entity.Quest;
import blaybus.happynewyear.calendar.entity.WeekCalendar;
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

    public Quest toQuest(String cycle, MonthCalendar monthCalendar) {
        return Quest.builder()
                .questName(ExpType.LEADER_QUEST.getName() + "(" + this.questName + ")")
                .comments(this.comments)
                .cycle(cycle)
                .monthCalendar(monthCalendar)
                .achievement(this.achievement)
                .build();
    }

    public Quest toQuest(String cycle, MonthCalendar monthCalendar, WeekCalendar weekCalendar) {
        return Quest.builder()
                .questName(ExpType.TEAM_QUEST.getName())
                .comments(this.comments)
                .cycle(cycle)
                .monthCalendar(monthCalendar)
                .weekCalendar(weekCalendar)
                .achievement(this.achievement)
                .build();
    }

}
