package blaybus.happynewyear.calendar.dto;

import blaybus.happynewyear.calendar.entity.Quest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuestDto {
    private String questName;
    private String comments;
    private String achievement;
    private int exp;

    public static QuestDto toDto(Quest quest) {
        return QuestDto.builder()
                .questName(quest.getQuestName())
                .comments(quest.getComments())
                .achievement(quest.getAchievement())
                .exp(quest.getExp())
                .build();
    }
}
