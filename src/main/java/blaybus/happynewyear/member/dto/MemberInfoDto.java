package blaybus.happynewyear.member.dto;

import blaybus.happynewyear.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class MemberInfoDto {
    private Long id;
    private String name;
    private String level;
    private String imgUrl;
    private String startDate;
    private String teamName;

    public static MemberInfoDto toDto(Member member) {
        return MemberInfoDto.builder()
                .id(member.getId())
                .name(member.getName())
                .level(member.getLevel())
                .imgUrl("/images/character" + member.getImgNumber() + ".png")
                .startDate(member.getStartDate())
                .teamName(member.getTeam().getTeamName())
                .build();
    }
}
