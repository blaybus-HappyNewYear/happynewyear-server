package blaybus.happynewyear.calendar.repository;

import blaybus.happynewyear.calendar.entity.MonthCalendar;
import blaybus.happynewyear.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MonthCalendarRepository extends JpaRepository<MonthCalendar, Integer> {
    List<MonthCalendar> findByMemberId(Long memberId);

    Optional<MonthCalendar> findByMemberAndYearAndMonth(Member member, int year, int month);
}
