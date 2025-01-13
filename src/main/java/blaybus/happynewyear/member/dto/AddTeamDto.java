package blaybus.happynewyear.member.dto;

import blaybus.happynewyear.member.entity.Team;
import lombok.Getter;

@Getter
public class AddTeamDto {
    private String teamName;
    private Integer teamNumber;

    public Team toEntity() {
        return Team.builder()
                .teamName(teamName)
                .teamNumber(teamNumber)
                .build();
    }
}
