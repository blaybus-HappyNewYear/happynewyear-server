package blaybus.happynewyear.calendar.repository;

import blaybus.happynewyear.calendar.entity.WeekCalendarEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeekCalendarRepository extends JpaRepository<WeekCalendarEntity, Long> {

    // 특정 연도의 데이터를 주차 기준으로 정렬하여 페이징
    Page<WeekCalendarEntity> findByYearOrderByWeekNumber(int year, Pageable pageable);
    long countByYear(int year);
}
