package blaybus.happynewyear.member.controller;

import blaybus.happynewyear.member.dto.AddTeamDto;
import blaybus.happynewyear.member.entity.Team;
import blaybus.happynewyear.member.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/add-team")
    public ResponseEntity<String> addTeam(@RequestBody AddTeamDto addTeamDto) {
        teamService.addTeam(addTeamDto);
        return ResponseEntity.ok("팀이 추가되었습니다.");
    }

    @PostMapping("/update-team")
    public ResponseEntity<String> updateTeam(@RequestBody AddTeamDto addTeamDto) {
        teamService.updateTeam(addTeamDto);
        return ResponseEntity.ok("팀이 수정되었습니다.");
    }

    @PostMapping("/delete-team")
    public ResponseEntity<String> deleteTeam(@RequestBody AddTeamDto addTeamDto) {
        teamService.deleteTeam(addTeamDto);
        return ResponseEntity.ok("팀이 삭제되었습니다.");
    }

}
