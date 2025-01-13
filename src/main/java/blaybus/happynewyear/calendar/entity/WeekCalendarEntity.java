package blaybus.happynewyear.calendar.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class WeekCalendarEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private int year;
    private int month;
    private int weekNumber;
    private int sundayDate;

    private boolean isFirst;

    private int questCount;

    private String achievement = "None"; // MAX, MEDIUM, NONE
}
