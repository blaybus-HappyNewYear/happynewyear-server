package blaybus.happynewyear.member.entity;

import blaybus.happynewyear.exp.entity.LeaderQuestType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Team {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String teamName;

    @Column(nullable = false)
    private int teamNumber;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<LeaderQuestType> leaderQuestTypes = new ArrayList<>();
}
