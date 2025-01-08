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
    private int imgNumber;
    private String startDate;
    private String team;

    public static MemberInfoDto toDto(Member member) {
        return MemberInfoDto.builder()
                .id(member.getId())
                .name(member.getName())
                .level(member.getLevel())
                .imgNumber(member.getImgNumber())
                .startDate(member.getStartDate())
                .team(member.getTeam())
                .build();
    }
}
