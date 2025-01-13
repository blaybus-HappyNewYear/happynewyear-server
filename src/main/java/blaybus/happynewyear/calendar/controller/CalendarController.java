package blaybus.happynewyear.calendar.controller;

import blaybus.happynewyear.calendar.entity.WeekCalendarEntity;
import blaybus.happynewyear.calendar.repository.WeekCalendarRepository;
import blaybus.happynewyear.calendar.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class CalendarController {

    private final CalendarService calendarService;
    private final WeekCalendarRepository weekCalendarRepository;

    // 초기 진입: 2025년 1월 첫 페이지 반환
    @GetMapping("/calendar")
    public ResponseEntity<Map<String, Object>> getDefaultPage(
            @RequestParam(defaultValue = "15") int size) {
        int defaultYear = 2025;
        int defaultPage = 0; // 첫 페이지
        return ResponseEntity.ok(calendarService.getPage(defaultYear, defaultPage, size));
    }

    @GetMapping("calendar/{year}/previous")
    public ResponseEntity<Map<String, Object>> getPreviousPage(
            @PathVariable int year,
            @RequestParam int page,
            @RequestParam(defaultValue = "15") int size) {
        return ResponseEntity.ok(calendarService.getPreviousPage(year, page, size));
    }

    @GetMapping("calendar/{year}/next")
    public ResponseEntity<Map<String, Object>> getNextPage(
            @PathVariable int year,
            @RequestParam int page,
            @RequestParam(defaultValue = "15") int size) {
        return ResponseEntity.ok(calendarService.getNextPage(year, page, size));
    }

    //캘린더 생성
    @GetMapping("/week-calendar")
    public ResponseEntity<String> getWeekCalendars(@RequestParam int startYear, @RequestParam int endYear) {
        for (int year = startYear; year <= endYear; year++) {
            List<WeekCalendarEntity> calendarData = calendarService.generateCalendar(year);
            weekCalendarRepository.saveAll(calendarData);
        }
        return ResponseEntity.ok("주간 달력이 저장되었습니다");
    }
}
