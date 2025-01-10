package blaybus.happynewyear.exp.service;

import blaybus.happynewyear.exp.dto.request.LeaderQuestDto;
import blaybus.happynewyear.exp.dto.request.PerfEvalDto;
import blaybus.happynewyear.exp.dto.request.TeamQuestDto;
import blaybus.happynewyear.exp.dto.request.TfProjectDto;
import blaybus.happynewyear.exp.dto.request.LeaderQuestTypeDto;

public interface ExpRequestService {
    void addLeaderQuestType(LeaderQuestTypeDto leaderQuestTypeDto);
    void addLeaderQuest(LeaderQuestDto leaderQuestDto);
    void addTeamQuest(TeamQuestDto teamQuestDto);
    void addTfProject(TfProjectDto tfProjectDto);
    void addPerfEval(PerfEvalDto perfEvalDto);

}
