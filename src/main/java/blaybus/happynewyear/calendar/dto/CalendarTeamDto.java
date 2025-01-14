package blaybus.happynewyear.calendar.dto;

import blaybus.happynewyear.member.entity.Team;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CalendarTeamDto {
    private String teamName;

    public static CalendarTeamDto toDto(Team team) {
        return CalendarTeamDto.builder()
                .teamName(team.getTeamName() + " " + team.getTeamNumber() + "그룹")
                .build();
    }
}
