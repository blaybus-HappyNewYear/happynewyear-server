package blaybus.happynewyear.exp.repository;

import blaybus.happynewyear.exp.entity.LeaderQuestType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeaderQuestTypeRepository extends JpaRepository<LeaderQuestType, Long> {
    Optional<LeaderQuestType> findByQuestName(String questName);
}
