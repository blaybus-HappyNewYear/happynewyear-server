package blaybus.happynewyear.member.service.Impl;

import blaybus.happynewyear.config.error.ErrorCode;
import blaybus.happynewyear.config.error.exception.BusinessException;
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

    @Override
    @Transactional
    public void updateTeam(AddTeamDto addTeamDto) {
        Team team = teamRepository.findByTeamNameAndTeamNumber(addTeamDto.getTeamName(), addTeamDto.getTeamNumber()).orElseThrow(()->new BusinessException(ErrorCode.TEAM_NOT_FOUND));
        if (addTeamDto.getTeamName() != null) team.setTeamName(addTeamDto.getTeamName());
        if (addTeamDto.getTeamNumber() != null) team.setTeamNumber(addTeamDto.getTeamNumber());
    }

    @Override
    @Transactional
    public void deleteTeam(AddTeamDto addTeamDto){
        Team team = teamRepository.findByTeamNameAndTeamNumber(addTeamDto.getTeamName(), addTeamDto.getTeamNumber()).orElseThrow(()->new BusinessException(ErrorCode.TEAM_NOT_FOUND));
        teamRepository.delete(team);
    }

}
