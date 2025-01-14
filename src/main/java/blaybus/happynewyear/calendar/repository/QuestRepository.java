package blaybus.happynewyear.calendar.repository;

import blaybus.happynewyear.calendar.entity.MonthCalendar;
import blaybus.happynewyear.calendar.entity.Quest;
import blaybus.happynewyear.calendar.entity.WeekCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestRepository extends JpaRepository<Quest, Integer> {
}
