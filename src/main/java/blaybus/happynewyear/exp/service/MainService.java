package blaybus.happynewyear.exp.service;

import blaybus.happynewyear.exp.dto.reponse.AllExpDto;
import blaybus.happynewyear.exp.dto.reponse.RecentExpDto;
import blaybus.happynewyear.member.dto.CharacterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MainService {
    List<RecentExpDto> getRecentExp(String accessToken);
    Page<AllExpDto> getAllExp(String accessToken, Pageable pageable);
    CharacterDto getCharacter(String accessToken);

}
