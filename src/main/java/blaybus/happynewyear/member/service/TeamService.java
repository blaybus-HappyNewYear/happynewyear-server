package blaybus.happynewyear.member.service;


import blaybus.happynewyear.member.dto.AddTeamDto;

public interface TeamService {
    void addTeam(AddTeamDto addTeamDto);
    void updateTeam(AddTeamDto addTeamDto);
    void deleteTeam(AddTeamDto addTeamDto);
}
