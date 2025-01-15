package blaybus.happynewyear.admin.dto;

import blaybus.happynewyear.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberDetailDto {
    private Long id;
    private String name;
    private String startDate;
    private String teamName;
    private int teamNumber;
    private String level;

    public static MemberDetailDto toDto(Member member) {
        return MemberDetailDto.builder()
                .id(member.getId())
                .name(member.getName())
                .startDate(member.getStartDate())
                .teamName(member.getTeam().getTeamName())
                .teamNumber(member.getTeam().getTeamNumber())
                .level(member.getLevel())
                .build();
    }
}
