package blaybus.happynewyear.exp.entity;

import blaybus.happynewyear.member.entity.Team;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LeaderQuestType {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String questName;
    private String cycle;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
}
