package blaybus.happynewyear.exp.repository;

import blaybus.happynewyear.exp.entity.Exp;
import blaybus.happynewyear.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpRepository extends JpaRepository<Exp, Long> {
    Page<Exp> findByMember(Member member, Pageable pageable);
    List<Exp> findTop5ByMemberOrderByIdDesc(Member member);
}
