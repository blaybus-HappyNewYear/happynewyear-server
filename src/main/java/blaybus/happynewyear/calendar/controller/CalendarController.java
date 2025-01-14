package blaybus.happynewyear.calendar.controller;

import blaybus.happynewyear.calendar.dto.CalendarTeamDto;
import blaybus.happynewyear.calendar.dto.MonthCalendarDto;
import blaybus.happynewyear.calendar.dto.WeekCalendarDto;
import blaybus.happynewyear.calendar.repository.WeekCalendarRepository;
import blaybus.happynewyear.calendar.service.CalendarService;
import blaybus.happynewyear.member.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class CalendarController {

    private final CalendarService calendarService;
    private final WeekCalendarRepository weekCalendarRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // weekly 캘린더 목록 가져오기
    @GetMapping("calendar/weekly")
    public ResponseEntity<List<WeekCalendarDto>> getWeeklyCalendar(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        List<WeekCalendarDto> weekCalendarDtos = calendarService.getWeeklyCalendars(accessToken);
        return ResponseEntity.ok(weekCalendarDtos);
    }

    // monthly 캘린더 목록 가져오기
    @GetMapping("calendar/monthly")
    public ResponseEntity<List<MonthCalendarDto>> getMonthlyCalendar(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        List<MonthCalendarDto> monthCalendarDtos = calendarService.getMonthlyCalendars(accessToken);
        return ResponseEntity.ok(monthCalendarDtos);
    }

    // 소속 가져오기
    @GetMapping("/calendar/team")
    public ResponseEntity<CalendarTeamDto> getCalendarTeam(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        CalendarTeamDto calendarTeam = calendarService.getCalendarTeam(accessToken);
        return ResponseEntity.ok(calendarTeam);
    }

    // 리더 부여 퀘스트 목록
    @GetMapping("calendar/quest-type")
    public ResponseEntity<List<String>> getQuestType(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resloveAccessToken(request);
        List<String> questTypes = calendarService.getQuestType(accessToken);
        return ResponseEntity.ok(questTypes);
    }


    /*
    // 초기 진입: 2025년 1월 첫 페이지 반환
    @GetMapping("/calendar")
    public ResponseEntity<Map<String, Object>> getDefaultPage(HttpServletRequest request,
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

     */

    /*
    //캘린더 생성
    @GetMapping("/week-calendar")
    public ResponseEntity<String> getWeekCalendars(@RequestParam int startYear, @RequestParam int endYear) {
        for (int year = startYear; year <= endYear; year++) {
            List<WeekCalendarEntity> calendarData = calendarService.generateWeeklyCalendar(year);
            weekCalendarRepository.saveAll(calendarData);
        }
        return ResponseEntity.ok("주간 달력이 저장되었습니다");
    }
     */
}
