package blaybus.happynewyear.member.dto;

import lombok.Getter;

@Getter
public class PasswordUpdateDto {
    private String oldPassword;
    private String newPassword;
}
