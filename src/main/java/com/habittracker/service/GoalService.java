package com.habittracker.service;

import com.habittracker.domain.Goal;
import com.habittracker.mapper.GoalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalMapper goalMapper;

    public Optional<Goal> getGoalById(Long id) {
        return goalMapper.findById(id);
    }

    public List<Goal> getGoalsByUserId(Long userId) {
        return goalMapper.findByUserId(userId);
    }

    public List<Goal> getActiveGoalsByUserId(Long userId) {
        return goalMapper.findActiveByUserId(userId);
    }

    @Transactional
    public Goal createGoal(Goal goal) {
        if (goal.getActive() == null) {
            goal.setActive(true);
        }
        goalMapper.insert(goal);
        return goal;
    }

    @Transactional
    public Goal updateGoal(Goal goal) {
        goalMapper.update(goal);
        return goalMapper.findById(goal.getId()).orElse(null);
    }

    @Transactional
    public void deleteGoal(Long id) {
        goalMapper.delete(id);
    }
}

