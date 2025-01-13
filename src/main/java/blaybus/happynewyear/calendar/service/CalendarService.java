package blaybus.happynewyear.calendar.service;

import blaybus.happynewyear.calendar.dto.MonthCalendarDto;
import blaybus.happynewyear.calendar.dto.WeekCalendarDto;
import blaybus.happynewyear.calendar.entity.MonthCalendar;
import blaybus.happynewyear.calendar.entity.WeekCalendar;
import blaybus.happynewyear.member.entity.Member;

import java.util.List;

public interface CalendarService {

    /*
    Map<String, Object> getPage(int year, int page, int size);
    Map<String, Object> getPreviousPage(int year, int page, int size);
    Map<String, Object> getNextPage(int year, int page, int size);

     */

    List<WeekCalendarDto> getWeeklyCalendars(String accessToken);
    List<MonthCalendarDto> getMonthlyCalendars(String accessToken);
    // 캘린더 데이터 저장하기
    List<WeekCalendar> generateWeeklyCalendar(int year, Member member);

    List<MonthCalendar> generateMonthlyCalendar(int year, Member member);

}
