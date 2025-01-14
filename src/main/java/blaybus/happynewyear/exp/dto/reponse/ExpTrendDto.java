package blaybus.happynewyear.exp.dto.reponse;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class ExpTrendDto {
    private Map<Integer, Integer> expData;
}
