package blaybus.happynewyear.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberUpdateDto {
    private Long id;
    private String username;
    private String name;
    private String startDate;
    private String team;
    private Integer teamNumber;
    private String jobGroup;
    private String level;
    private String password;
    private Integer imgNumber;


}