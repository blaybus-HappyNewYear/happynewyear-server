package blaybus.happynewyear.exp.service;

import blaybus.happynewyear.exp.dto.reponse.ExpTrendDto;

public interface ExpInfoService {
    ExpTrendDto getExpTrend(String accessToken);
}
