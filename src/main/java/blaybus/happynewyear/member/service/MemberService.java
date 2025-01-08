package blaybus.happynewyear.member.service;

import blaybus.happynewyear.member.dto.JwtToken;
import blaybus.happynewyear.member.dto.MemberInfoDto;
import blaybus.happynewyear.member.dto.SignUpDto;

public interface MemberService {

    JwtToken signIn(String memberId, String password);
    void signUp(SignUpDto signUpDto);
    MemberInfoDto getMemberInfo(String accessToken);
}
