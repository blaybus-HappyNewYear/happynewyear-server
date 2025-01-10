package blaybus.happynewyear.exp.dto.reponse;

import blaybus.happynewyear.exp.entity.Exp;
import blaybus.happynewyear.exp.entity.TeamExp;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
