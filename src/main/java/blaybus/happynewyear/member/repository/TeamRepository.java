package blaybus.happynewyear.member.repository;

import blaybus.happynewyear.member.entity.Member;
import blaybus.happynewyear.member.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByTeamNameAndTeamNumber(String teamName, int teamNumber);
}
