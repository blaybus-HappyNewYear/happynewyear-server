package blaybus.happynewyear.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CharacterDto {
    private int imgNumber;
    private String imgUrl;

    public static CharacterDto toDto(int imgNumber) {
        return CharacterDto.builder()
                .imgNumber(imgNumber)
                .imgUrl("/images/character" + imgNumber + ".png")
                .build();
    }
}
