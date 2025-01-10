package blaybus.happynewyear.exp.controller;

import blaybus.happynewyear.exp.dto.request.*;
import blaybus.happynewyear.exp.service.ExpRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ExpRequestController {

    private final ExpRequestService expService;

    // 리더부여 퀘스트 종류 추가
    @PostMapping("/add-type/leader-quest")
    public ResponseEntity<String> addLeaderQuestType(@RequestBody LeaderQuestTypeDto leaderQuestTypeDto) {
        expService.addLeaderQuestType(leaderQuestTypeDto);
        return ResponseEntity.ok("리더부여 퀘스트 '" + leaderQuestTypeDto.getQuestName() + "'(이)가 추가되었습니다.");
    }

    // add team quest
    @PostMapping("/add-exp/team-quest")
    public ResponseEntity<String> addTeamQuest(@RequestBody TeamQuestDto teamQuestDto) {
        expService.addTeamQuest(teamQuestDto);
        return ResponseEntity.ok("직무별 퀘스트 경험치가 부여되었습니다.");
    }

    // add leader quest
    @PostMapping("/add-exp/leader-quest")
    public ResponseEntity<String> addLeaderQuest(@RequestBody LeaderQuestDto leaderQuestDto) {
        expService.addLeaderQuest(leaderQuestDto);
        return ResponseEntity.ok("리더 부여 퀘스트 경험치가 부여되었습니다.");
    }

    // add tf project
    @PostMapping("/add-exp/tf-project")
    public ResponseEntity<String> addTfProject(@RequestBody TfProjectDto tfProjectDto) {
        expService.addTfProject(tfProjectDto);
        return ResponseEntity.ok("TF 프로젝트 경험치가 부여되었습니다.");
    }

    // 인사평가
    @PostMapping("/add-exp/perf-eval")
    public ResponseEntity<String> addPerfEval(@RequestBody PerfEvalDto perfEvalDto) {
        expService.addPerfEval(perfEvalDto);
        return ResponseEntity.ok("인사평가가 추가되었습니다.");
    }

}
