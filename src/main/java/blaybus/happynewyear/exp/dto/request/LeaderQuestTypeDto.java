package blaybus.happynewyear.exp.dto.request;

import blaybus.happynewyear.exp.entity.LeaderQuestType;
import lombok.Getter;

@Getter
public class LeaderQuestTypeDto {
    private String questName;
    private String cycle;

    public LeaderQuestType toEntity() {
        return LeaderQuestType.builder()
                .questName(questName)
                .cycle(cycle)
                .build();
    }
}
