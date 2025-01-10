package blaybus.happynewyear.member.service.Impl;

import blaybus.happynewyear.member.dto.AddTeamDto;
import blaybus.happynewyear.member.entity.Team;
import blaybus.happynewyear.member.repository.TeamRepository;
import blaybus.happynewyear.member.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public void addTeam(AddTeamDto addTeamDto) {
        teamRepository.save(addTeamDto.toEntity());
    }
}
