package blaybus.happynewyear.calendar.entity;

import blaybus.happynewyear.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MonthCalendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private int year;
    private int month;

    private int questCount = 0;

    private String achievement = "None"; // MAX, MEDIUM, NONE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}
