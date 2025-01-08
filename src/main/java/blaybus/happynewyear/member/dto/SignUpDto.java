package blaybus.happynewyear.member.dto;

import blaybus.happynewyear.member.entity.Member;
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
    private String team;
    private int teamNumber;
    private String jobGroup;
    private String level;
    private int imgNumber;

    public Member toEntity(String encodedPassword, List<String> roles) {
        return Member.builder()
                .id(id)
                .username(username)
                .password(encodedPassword)
                .name(name)
                .startDate(startDate)
                .team(team)
                .teamNumber(teamNumber)
                .jobGroup(jobGroup)
                .level(level)
                .imgNumber(imgNumber)
                .roles(roles)
                .build();
    }
}
