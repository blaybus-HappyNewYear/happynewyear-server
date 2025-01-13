package blaybus.happynewyear.calendar.service.Impl;

import blaybus.happynewyear.calendar.dto.MonthCalendarDto;
import blaybus.happynewyear.calendar.dto.WeekCalendarDto;
import blaybus.happynewyear.calendar.entity.MonthCalendar;
import blaybus.happynewyear.calendar.entity.WeekCalendar;
import blaybus.happynewyear.calendar.repository.MonthCalendarRepository;
import blaybus.happynewyear.calendar.repository.WeekCalendarRepository;
import blaybus.happynewyear.calendar.service.CalendarService;
import blaybus.happynewyear.config.error.ErrorCode;
import blaybus.happynewyear.config.error.exception.BusinessException;
import blaybus.happynewyear.member.entity.Member;
import blaybus.happynewyear.member.jwt.JwtTokenProvider;
import blaybus.happynewyear.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final WeekCalendarRepository weekCalendarRepository;
    private final MonthCalendarRepository monthCalendarRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public List<WeekCalendarDto> getWeeklyCalendars(String accessToken) {
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        String username = claims.getSubject();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return weekCalendarRepository.findByMemberId(member.getId()).stream()
                .map(WeekCalendarDto::toDto)
                .toList();
    }

    @Override
    @Transactional
    public List<MonthCalendarDto> getMonthlyCalendars(String accessToken) {
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        String username = claims.getSubject();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return monthCalendarRepository.findByMemberId(member.getId()).stream()
                .map(MonthCalendarDto::toDto)
                .toList();

    }

    // weekly 캘린더 데이터 저장하기
    @Override
    @Transactional
    public List<WeekCalendar> generateWeeklyCalendar(int year, Member member) {

        List<WeekCalendar> weekCalendarData = new ArrayList<>();
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        TemporalField weekOfYear = WeekFields.of(Locale.getDefault()).weekOfYear();

        Map<Integer, Boolean> monthFirstWeeks = new HashMap<>();

        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusWeeks(1)) {
            int weekNumber = date.get(weekOfYear);

            // 해당 주의 일요일 계산
            LocalDate sundayOfWeek = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            // **일요일이 다음 연도로 넘어가면 해당 주차 제외**
            if (sundayOfWeek.getYear() != year) {
                break;
            }
            int month = sundayOfWeek.getMonthValue(); // **일요일 기준으로 월 계산**
            int sundayDate = sundayOfWeek.getDayOfMonth();

            WeekCalendar entity = new WeekCalendar();
            entity.setYear(year);
            entity.setMonth(month); // **수정된 월 값 설정**
            entity.setWeekNumber(weekNumber);
            entity.setSundayDate(sundayDate);
            entity.setMember(member);

            if (!monthFirstWeeks.containsKey(month)) {
                entity.setFirst(true);
                monthFirstWeeks.put(month, true);
            } else {
                entity.setFirst(false);
            }

            weekCalendarData.add(entity);
        }

        return weekCalendarData;
    }

    @Override
    @Transactional
    public List<MonthCalendar> generateMonthlyCalendar(int year, Member member) {
        List<MonthCalendar> monthCalendarData = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            MonthCalendar monthCalendar = new MonthCalendar();
            monthCalendar.setYear(year);
            monthCalendar.setMonth(month);
            monthCalendar.setMember(member);

            monthCalendarData.add(monthCalendar);
        }
        return monthCalendarData;
    }

    /*
    // 기본 페이지 반환
    @Override
    @Transactional
    public Map<String, Object> getPage(int year, int page, int size) {
        Page<WeekCalendar> calendarPage = weekCalendarRepository.findByYearOrderByWeekNumber(year, PageRequest.of(page, size));
        return createPageResponse(year, page, calendarPage);
    }

    // 이전 페이지 처리
    @Override
    @Transactional
    public Map<String, Object> getPreviousPage(int year, int page, int size) {
        if (page == 0) {
            int previousYear = year - 1;
            int totalPages = getTotalPages(previousYear, size);
            if (totalPages > 0) {
                Page<WeekCalendar> lastPage = weekCalendarRepository.findByYearOrderByWeekNumber(previousYear, PageRequest.of(totalPages - 1, size));
                return createPageResponse(previousYear, totalPages - 1, lastPage);
            }
        } else {
            Page<WeekCalendar> previousPage = weekCalendarRepository.findByYearOrderByWeekNumber(year, PageRequest.of(page - 1, size));
            return createPageResponse(year, page - 1, previousPage);
        }
        return createEmptyResponse(year, page);
    }


    // 다음 페이지 처리
    @Override
    @Transactional
    public Map<String, Object> getNextPage(int year, int page, int size) {
        int totalPages = getTotalPages(year, size);
        if (page + 1 >= totalPages) {
            int nextYear = year + 1;
            Page<WeekCalendar> firstPage = weekCalendarRepository.findByYearOrderByWeekNumber(nextYear, PageRequest.of(0, size));
            return createPageResponse(nextYear, 0, firstPage);
        } else {
            Page<WeekCalendar> nextPage = weekCalendarRepository.findByYearOrderByWeekNumber(year, PageRequest.of(page + 1, size));
            return createPageResponse(year, page + 1, nextPage);
        }
    }

    // 총 페이지 수 계산
    private int getTotalPages(int year, int size) {
        long totalElements = weekCalendarRepository.countByYear(year);
        return (int) Math.ceil((double) totalElements / size);
    }

    // 페이지 응답 생성
    private Map<String, Object> createPageResponse(int year, int page, Page<WeekCalendar> data) {
        Map<String, Object> response = new HashMap<>();
        response.put("year", year);
        response.put("page", page);
        response.put("data", data.getContent());
        return response;
    }

    // 빈 페이지 응답 생성
    private Map<String, Object> createEmptyResponse(int year, int page) {
        Map<String, Object> response = new HashMap<>();
        response.put("year", year);
        response.put("page", page);
        response.put("data", Collections.emptyList());
        return response;
    }

     */


}
