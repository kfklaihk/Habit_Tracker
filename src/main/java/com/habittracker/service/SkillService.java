package com.habittracker.service;

import com.habittracker.domain.Skill;
import com.habittracker.mapper.SkillMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillMapper skillMapper;

    public SkillService(SkillMapper skillMapper) {
        this.skillMapper = skillMapper;
    }

    public List<Skill> getAllSkills() {
        return skillMapper.findAll();
    }

    public Optional<Skill> getSkillById(Long id) {
        return skillMapper.findById(id);
    }

    @Transactional
    public Skill createSkill(Skill skill) {
        skillMapper.insert(skill);
        return skill;
    }

    @Transactional
    public Skill updateSkill(Skill skill) {
        skillMapper.update(skill);
        return skillMapper.findById(skill.getId()).orElse(null);
    }

    @Transactional
    public void deleteSkill(Long id) {
        skillMapper.delete(id);
    }
}

