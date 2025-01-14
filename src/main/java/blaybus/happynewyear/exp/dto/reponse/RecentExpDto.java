package blaybus.happynewyear.exp.dto.reponse;

import blaybus.happynewyear.exp.entity.Exp;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class RecentExpDto {
    private String type;
    private int exp;
    private LocalDate earnedDate;

    public static RecentExpDto expToDto(Exp exp) {
        return RecentExpDto.builder()
                .type(exp.getType())
                .exp(exp.getExp())
                .earnedDate(exp.getEarnedDate())
                .build();
    }
}
