package blaybus.happynewyear.exp.enums;

import lombok.Getter;

@Getter
public enum ExpType {
    LEADER_QUEST("리더 부여 퀘스트"),
    TEAM_QUEST("직무별 퀘스트"),
    PERFORMANCE_EVALUATION("인사 평가"),
    TF_PROJECT("전사 프로젝트");

    private final String name;

    ExpType(String name) {
        this.name = name;
    }
}
