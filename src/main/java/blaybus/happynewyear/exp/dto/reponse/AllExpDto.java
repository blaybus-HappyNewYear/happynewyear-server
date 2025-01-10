package blaybus.happynewyear.exp.dto.reponse;

import blaybus.happynewyear.exp.entity.Exp;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class AllExpDto {
    private String type;
    private int exp;
    private LocalDate earnedDate;
    private String comments;

    public static AllExpDto expToDto(Exp exp) {
        return AllExpDto.builder()
                .type(exp.getType())
                .exp(exp.getExp())
                .earnedDate(exp.getEarnedDate())
                .comments(exp.getComments())
                .build();
    }
}
