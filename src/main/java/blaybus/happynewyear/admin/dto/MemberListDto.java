package blaybus.happynewyear.admin.dto;

import blaybus.happynewyear.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberListDto {
    private Long id;
    private String name;

    public static MemberListDto toDto(Member member) {
        return MemberListDto.builder()
                .id(member.getId())
                .name(member.getName())
                .build();
    }
}
