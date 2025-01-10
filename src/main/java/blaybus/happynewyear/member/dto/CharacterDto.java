package blaybus.happynewyear.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CharacterDto {
    private String imgUrl;

    public static CharacterDto toDto(int imgNumber) {
        return CharacterDto.builder()
                .imgUrl("/images/character" + imgNumber + ".png")
                .build();
    }
}
