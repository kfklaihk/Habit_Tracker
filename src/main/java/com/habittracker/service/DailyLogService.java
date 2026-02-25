package com.habittracker.service;

import com.habittracker.domain.DailyLog;
import com.habittracker.domain.LogSkill;
import com.habittracker.dto.DailyLogUpsertRequest;
import com.habittracker.mapper.DailyLogMapper;
import com.habittracker.mapper.LogSkillMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailyLogService {

    private final DailyLogMapper dailyLogMapper;
    private final LogSkillMapper logSkillMapper;

    public DailyLogService(DailyLogMapper dailyLogMapper, LogSkillMapper logSkillMapper) {
        this.dailyLogMapper = dailyLogMapper;
        this.logSkillMapper = logSkillMapper;
    }

    public Optional<DailyLog> getDailyLogById(Long id) {
        return dailyLogMapper.findById(id);
    }

    public List<DailyLog> getDailyLogs(Long userId, LocalDate startDate, LocalDate endDate) {
        return dailyLogMapper.findByUserIdAndDateRange(userId, startDate, endDate);
    }

    @Transactional
    public DailyLog upsertDailyLog(DailyLogUpsertRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request body is required");
        }
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("userId is required");
        }
        if (request.getLogDate() == null) {
            throw new IllegalArgumentException("logDate is required");
        }
        if (request.getHoursCoded() == null) {
            throw new IllegalArgumentException("hoursCoded is required");
        }
        if (request.getHoursCoded().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("hoursCoded must be >= 0");
        }

        DailyLog dailyLog = dailyLogMapper
                .findByUserIdAndDate(request.getUserId(), request.getLogDate())
                .orElseGet(DailyLog::new);

        dailyLog.setUserId(request.getUserId());
        dailyLog.setLogDate(request.getLogDate());
        dailyLog.setHoursCoded(request.getHoursCoded());
        dailyLog.setFocusScore(request.getFocusScore());
        dailyLog.setNotes(request.getNotes());

        if (dailyLog.getId() == null) {
            dailyLogMapper.insert(dailyLog);
        } else {
            dailyLogMapper.update(dailyLog);
        }

        // Replace skill minutes for this day
        logSkillMapper.deleteByDailyLogId(dailyLog.getId());
        if (request.getSkills() != null) {
            for (DailyLogUpsertRequest.SkillMinutes sm : request.getSkills()) {
                if (sm == null || sm.getSkillId() == null) {
                    continue;
                }
                if (sm.getMinutesPracticed() == null || sm.getMinutesPracticed() <= 0) {
                    continue;
                }
                LogSkill ls = new LogSkill();
                ls.setDailyLogId(dailyLog.getId());
                ls.setSkillId(sm.getSkillId());
                ls.setMinutesPracticed(sm.getMinutesPracticed());
                logSkillMapper.insert(ls);
            }
        }

        return dailyLogMapper.findById(dailyLog.getId()).orElse(dailyLog);
    }
}

