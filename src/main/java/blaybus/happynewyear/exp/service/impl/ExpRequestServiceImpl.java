package blaybus.happynewyear.exp.service.impl;

import blaybus.happynewyear.config.error.ErrorCode;
import blaybus.happynewyear.config.error.exception.BusinessException;
import blaybus.happynewyear.exp.dto.request.LeaderQuestDto;
import blaybus.happynewyear.exp.dto.request.PerfEvalDto;
import blaybus.happynewyear.exp.dto.request.TeamQuestDto;
import blaybus.happynewyear.exp.dto.request.TfProjectDto;
import blaybus.happynewyear.exp.dto.request.LeaderQuestTypeDto;
import blaybus.happynewyear.exp.entity.LeaderQuestType;
import blaybus.happynewyear.exp.repository.ExpRepository;
import blaybus.happynewyear.exp.repository.LeaderQuestTypeRepository;
import blaybus.happynewyear.exp.repository.TeamExpRepository;
import blaybus.happynewyear.exp.service.ExpRequestService;
import blaybus.happynewyear.member.entity.Member;
import blaybus.happynewyear.member.entity.Team;
import blaybus.happynewyear.member.repository.MemberRepository;
import blaybus.happynewyear.member.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpRequestServiceImpl implements ExpRequestService {

    private final ExpRepository expRepository;
    private final TeamExpRepository teamExpRepository;
    private final LeaderQuestTypeRepository leaderQuestTypeRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public void addLeaderQuestType(LeaderQuestTypeDto leaderQuestTypeDto) {
        leaderQuestTypeRepository.save(leaderQuestTypeDto.toEntity());
    }

    @Override
    @Transactional
    public void addLeaderQuest(LeaderQuestDto leaderQuestDto) {
        //존재하는 퀘스트인지 검증
        log.info("quest name: {}", leaderQuestDto.getQuestName());
        log.info("member name: {}", leaderQuestDto.getMemberName());
        LeaderQuestType leaderQuestType = leaderQuestTypeRepository.findByQuestName(leaderQuestDto.getQuestName())
                .orElseThrow(() -> new BusinessException(ErrorCode.QUEST_NOT_FOUND));
        //사번에 해당하는 멤버 찾기
        Member member = memberRepository.findById(leaderQuestDto.getMemberId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        LocalDate now = LocalDate.now();
        expRepository.save(leaderQuestDto.toExp(member, now));
    }

    @Override
    @Transactional
    public void addTeamQuest(TeamQuestDto teamQuestDto) {
        //존재하는 팀인지 검증
        Team team = teamRepository.findByTeamNameAndTeamNumber(teamQuestDto.getTeamName(), teamQuestDto.getTeamNumber())
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_NOT_FOUND));
        LocalDate now = LocalDate.now();
        teamExpRepository.save(teamQuestDto.toTeamExp(team, now));

        //팀에 해당하는 멤버에게 team quest exp를 전부 저장함

        List<Member> members = memberRepository.findByTeam(team);

        for (Member member : members) {
            expRepository.save(teamQuestDto.toExp(member, now));
        }
    }

    @Override
    @Transactional
    public void addTfProject(TfProjectDto tfProjectDto) {
        Member member = memberRepository.findById(tfProjectDto.getMemberId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        LocalDate now = LocalDate.now();
        expRepository.save(tfProjectDto.toEntity(member, now));
    }

    @Override
    @Transactional
    public void addPerfEval(PerfEvalDto perfEvalDto) {
        Member member = memberRepository.findById(perfEvalDto.getMemberId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        LocalDate now = LocalDate.now();
        expRepository.save(perfEvalDto.toEntity(member, now));
    }
}
