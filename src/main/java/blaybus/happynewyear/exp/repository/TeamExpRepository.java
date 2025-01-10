package blaybus.happynewyear.exp.repository;

import blaybus.happynewyear.exp.entity.TeamExp;
import blaybus.happynewyear.member.entity.Team;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamExpRepository extends JpaRepository<TeamExp, Long> {
    List<TeamExp> findByTeam(Team team);
    //List<TeamExp> findTop5ByTeamOrderByEarnedDateDesc(Team team, Pageable pageable);
}
