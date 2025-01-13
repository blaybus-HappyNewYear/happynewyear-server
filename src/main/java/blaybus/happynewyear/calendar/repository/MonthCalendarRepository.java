package blaybus.happynewyear.calendar.repository;

import blaybus.happynewyear.calendar.entity.MonthCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonthCalendarRepository extends JpaRepository<MonthCalendar, Integer> {
    List<MonthCalendar> findByMemberId(Long memberId);
}
