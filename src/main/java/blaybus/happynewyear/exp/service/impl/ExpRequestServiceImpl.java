package blaybus.happynewyear.exp.service.impl;

import blaybus.happynewyear.calendar.dto.QuestDto;
import blaybus.happynewyear.calendar.entity.MonthCalendar;
import blaybus.happynewyear.calendar.entity.WeekCalendar;
import blaybus.happynewyear.calendar.repository.MonthCalendarRepository;
import blaybus.happynewyear.calendar.repository.QuestRepository;
import blaybus.happynewyear.calendar.repository.WeekCalendarRepository;
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
import blaybus.happynewyear.exp.service.ExpRequestService;
import blaybus.happynewyear.member.entity.Member;
import blaybus.happynewyear.member.entity.Team;
import blaybus.happynewyear.member.repository.MemberRepository;
import blaybus.happynewyear.member.repository.TeamRepository;
import blaybus.happynewyear.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpRequestServiceImpl implements ExpRequestService {

    private final ExpRepository expRepository;
    private final LeaderQuestTypeRepository leaderQuestTypeRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final WeekCalendarRepository weekCalendarRepository;
    private final MonthCalendarRepository monthCalendarRepository;
    private final QuestRepository questRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public void addLeaderQuestType(LeaderQuestTypeDto leaderQuestTypeDto) {
        validateDuplicateType(leaderQuestTypeDto.getQuestName());
        LeaderQuestType save = leaderQuestTypeRepository.save(leaderQuestTypeDto.toEntity());

    }

    private void validateDuplicateType(String questName) {
        Optional<LeaderQuestType> findType = leaderQuestTypeRepository.findByQuestName(questName);
        if(findType.isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATED_QUEST);
        }
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
        // exp 저장
        expRepository.save(leaderQuestDto.toExp(member, now));
        // quest 저장
        String cycle = leaderQuestType.getCycle();
        // 퀘스트가 주간 퀘스트인 경우 (월간과 주간 캘린더에 전부 추가)
        if (cycle.equals("주")) {
            WeekCalendar weekCalendar = weekCalendarRepository
                    .findByMemberAndYearAndWeekNumber(member, 2025, leaderQuestDto.getWeek())
                    .orElseThrow(() -> new BusinessException(ErrorCode.CALENDAR_NOT_FOUND));
            MonthCalendar monthCalendar = monthCalendarRepository
                    .findByMemberAndYearAndMonth(member, 2025, weekCalendar.getMonth())
                    .orElseThrow(() -> new BusinessException(ErrorCode.CALENDAR_NOT_FOUND));

            //캘린더 achievement 변경
            String thisAchievement = leaderQuestDto.getAchievement();
            if (thisAchievement.equals("Max")) {
                weekCalendar.setAchievement("Max");
                monthCalendar.setAchievement("Max");
            } else if (thisAchievement.equals("Medium")) {
                if (weekCalendar.getAchievement().equals("None")) {
                    weekCalendar.setAchievement("Medium");
                }
                if (monthCalendar.getAchievement().equals("None")) {
                    monthCalendar.setAchievement("Medium");
                }
            }
            questRepository.save(leaderQuestDto.toQuest(cycle, monthCalendar, weekCalendar));
            // 퀘스트가 월간 퀘스트인 경우 (월간 캘린더에만 추가)
        } else if (cycle.equals("월")) {
            MonthCalendar monthCalendar = monthCalendarRepository
                    .findByMemberAndYearAndMonth(member, 2025, leaderQuestDto.getMonth())
                    .orElseThrow(() -> new BusinessException(ErrorCode.CALENDAR_NOT_FOUND));

            //캘린더 achievement 변경
            String thisAchievement = leaderQuestDto.getAchievement();
            if (thisAchievement.equals("Max")) {
                monthCalendar.setAchievement("Max");
            } else if (thisAchievement.equals("Medium")) {
                if (monthCalendar.getAchievement().equals("None")) {
                    monthCalendar.setAchievement("Medium");
                }
            }

            questRepository.save(leaderQuestDto.toQuest(cycle, monthCalendar));
            notificationService.createLeaderNotification(member.getId(), leaderQuestDto.getQuestName(), leaderQuestDto.getExp());
        }

    }

    @Override
    @Transactional
    public void addTeamQuest(TeamQuestDto teamQuestDto) {
        //존재하는 팀인지 검증
        Team team = teamRepository.findByTeamNameAndTeamNumber(teamQuestDto.getTeamName(), teamQuestDto.getTeamNumber())
                .orElseThrow(() -> new BusinessException(ErrorCode.TEAM_NOT_FOUND));
        LocalDate now = LocalDate.now();

        //팀에 해당하는 멤버에게 team quest exp를 전부 저장함

        List<Member> members = memberRepository.findByTeam(team);

        for (Member member : members) {
            expRepository.save(teamQuestDto.toExp(member, now));
        }

        //팀에 해당하는 멤버에게 해당 quest를 저장함
        for (Member member : members) {
            // 퀘스트가 주간 퀘스트인 경우 (월간과 주간 캘린더에 전부 추가)
            if (teamQuestDto.getCycle().equals("주")) {
                WeekCalendar weekCalendar = weekCalendarRepository
                        .findByMemberAndYearAndWeekNumber(member, 2025, teamQuestDto.getMonthOrWeek())
                        .orElseThrow(() -> new BusinessException(ErrorCode.CALENDAR_NOT_FOUND));
                MonthCalendar monthCalendar = monthCalendarRepository
                        .findByMemberAndYearAndMonth(member, 2025, weekCalendar.getMonth())
                        .orElseThrow(() -> new BusinessException(ErrorCode.CALENDAR_NOT_FOUND));

                //캘린더 achievement 변경
                String thisAchievement = teamQuestDto.getAchievement();
                if (thisAchievement.equals("Max")) {
                    weekCalendar.setAchievement("Max");
                    monthCalendar.setAchievement("Max");
                } else if (thisAchievement.equals("Medium")) {
                    if (weekCalendar.getAchievement().equals("None")) {
                        weekCalendar.setAchievement("Medium");
                    }
                    if (monthCalendar.getAchievement().equals("None")) {
                        monthCalendar.setAchievement("Medium");
                    }
                }

                questRepository.save(teamQuestDto.toQuest(weekCalendar, monthCalendar));
                // 퀘스트가 월간 퀘스트인 경우 (월간 캘린더에만 추가)
            } else if (teamQuestDto.getCycle().equals("월")) {
                MonthCalendar monthCalendar = monthCalendarRepository
                        .findByMemberAndYearAndMonth(member, 2025, teamQuestDto.getMonthOrWeek())
                        .orElseThrow(() -> new BusinessException(ErrorCode.CALENDAR_NOT_FOUND));

                //캘린더 achievement 변경
                String thisAchievement = teamQuestDto.getAchievement();
                if (thisAchievement.equals("Max")) {
                    monthCalendar.setAchievement("Max");
                } else if (thisAchievement.equals("Medium")) {
                    if (monthCalendar.getAchievement().equals("None")) {
                        monthCalendar.setAchievement("Medium");
                    }
                }

                questRepository.save(teamQuestDto.toQuest(monthCalendar));
                notificationService.createTeamNotification(member.getId(), teamQuestDto.getComments(), teamQuestDto.getExp());
            }
        }
    }

    @Override
    @Transactional
    public void addTfProject(TfProjectDto tfProjectDto) {
        Member member = memberRepository.findById(tfProjectDto.getMemberId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        LocalDate now = LocalDate.now();
        expRepository.save(tfProjectDto.toEntity(member, now));
        notificationService.createTfProjectNotification(member.getId(), tfProjectDto.getProjectName(), tfProjectDto.getExp());
    }

    @Override
    @Transactional
    public void addPerfEval(PerfEvalDto perfEvalDto) {
        Member member = memberRepository.findById(perfEvalDto.getMemberId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        LocalDate now = LocalDate.now();
        expRepository.save(perfEvalDto.toEntity(member, now));
        notificationService.createPerfEvalNotification(member.getId(), perfEvalDto.getGrade(), perfEvalDto.getExp());
    }
}
