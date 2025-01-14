package blaybus.happynewyear.calendar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Quest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questName;
    private String comments;
    private String achievement;
    private int exp;

    private String cycle;

    @ManyToOne
    @JoinColumn(name = "week_calendar_id")
    private WeekCalendar weekCalendar;

    @ManyToOne
    @JoinColumn(name = "month_calendar_id")
    private MonthCalendar monthCalendar;
}
