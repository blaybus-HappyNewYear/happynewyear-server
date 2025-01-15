package blaybus.happynewyear.member.dto;

import blaybus.happynewyear.member.entity.Member;
import blaybus.happynewyear.member.entity.Team;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class SignUpDto {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String startDate;
    private String teamName;
    private int teamNumber;
    private String jobGroup;
    private String level;

    public Member toEntity(String encodedPassword, Team team, List<String> roles) {
        return Member.builder()
                .id(this.id)
                .username(this.username)
                .password(encodedPassword)
                .name(this.name)
                .startDate(this.startDate)
                .team(team)
                .jobGroup(this.jobGroup)
                .level(this.level)
                .imgNumber(1)
                .roles(roles)
                .build();
    }
}
