package blaybus.happynewyear.exp.entity;

import blaybus.happynewyear.member.entity.Team;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamExp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false) // 외래 키 설정
    private Team team;

    private String cycle;
    private int monthOrWeek;
    private int exp;
    private LocalDate earnedDate;
    private String type;
}
