package blaybus.happynewyear.member.repository;

import blaybus.happynewyear.member.entity.Member;
import blaybus.happynewyear.member.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);

    List<Member> findByTeam(Team team);
}
