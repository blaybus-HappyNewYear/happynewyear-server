package blaybus.happynewyear.calendar.dto;

import blaybus.happynewyear.calendar.entity.MonthCalendar;
import blaybus.happynewyear.calendar.entity.WeekCalendar;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MonthCalendarDto {
    private Long id;

    private int year;
    private int month;
    private int questCount;

    private String achievement;

    private List<QuestDto> questList;

    public static MonthCalendarDto toDto(MonthCalendar monthCalendar) {
        List<QuestDto> questList = monthCalendar.getQuests().stream().map(QuestDto::toDto).toList();
        return MonthCalendarDto.builder()
                .id(monthCalendar.getId())
                .year(monthCalendar.getYear())
                .month(monthCalendar.getMonth())
                .achievement(monthCalendar.getAchievement())
                .questList(questList)
                .questCount(questList.size())
                .build();
    }
}
