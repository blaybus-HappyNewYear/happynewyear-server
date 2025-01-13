package blaybus.happynewyear.calendar.dto;

import blaybus.happynewyear.calendar.entity.WeekCalendar;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class WeekCalendarDto {
    private Long id;

    private int year;
    private int month;
    private int date;

    private int questCount;

    private String achievement;

    private List<QuestDto> questList;

    public static WeekCalendarDto toDto(WeekCalendar weekCalendar) {
        return WeekCalendarDto.builder()
                .id(weekCalendar.getId())
                .year(weekCalendar.getYear())
                .month(weekCalendar.getMonth())
                .date(weekCalendar.getSundayDate())
                .achievement(weekCalendar.getAchievement())
                .build();
    }
}
