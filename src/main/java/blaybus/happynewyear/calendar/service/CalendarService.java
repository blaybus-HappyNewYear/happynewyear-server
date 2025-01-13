package blaybus.happynewyear.calendar.service;

import blaybus.happynewyear.calendar.entity.WeekCalendarEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface CalendarService {
    Map<String, Object> getPage(int year, int page, int size);
    Map<String, Object> getPreviousPage(int year, int page, int size);
    Map<String, Object> getNextPage(int year, int page, int size);

    // 캘린더 데이터 저장하기
    List<WeekCalendarEntity> generateCalendar(int year);

}
