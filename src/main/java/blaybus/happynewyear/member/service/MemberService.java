package blaybus.happynewyear.member.service;

import blaybus.happynewyear.member.dto.*;
import blaybus.happynewyear.member.entity.Member;

public interface MemberService {

    JwtToken signIn(String memberId, String password);
    void signUp(SignUpDto signUpDto);
    void signOut(String accessToken);
    MemberInfoDto getMemberInfo(String accessToken);
    void updatePassword(String accessToken, PasswordUpdateDto passwordUpdateDto);
    void updateCharacter(String accessToken, int imgNumber);

    void updateMember(MemberUpdateDto memberUpdateDto);

    boolean memberExists(String username);


    void deleteMember(String username);
}
