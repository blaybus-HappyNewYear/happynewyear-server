package blaybus.happynewyear.exp.dto.request;

import blaybus.happynewyear.exp.entity.LeaderQuestType;
import blaybus.happynewyear.member.entity.Team;
import lombok.Getter;

@Getter
public class LeaderQuestTypeDto {
    private String questName;
    private String cycle;
    private String teamName;

    public LeaderQuestType toEntity() {
        return LeaderQuestType.builder()
                .questName(questName)
                .cycle(cycle)
                .teamName(teamName)
                .build();
    }
}
