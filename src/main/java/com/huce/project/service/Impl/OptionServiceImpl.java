package com.huce.project.service.Impl;

import org.springframework.stereotype.Service;

import com.huce.project.entity.OptionEntity;
import com.huce.project.repository.OptionRepository;
import com.huce.project.service.OptionService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {

    private final OptionRepository optionRepository;

    @Override
    public OptionEntity createOption(OptionEntity option) {
        return optionRepository.save(option);
    }

    @Override
    public OptionEntity updateOption(int id, OptionEntity option) {
        OptionEntity existing = optionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Option not found with id: " + id));
        existing.setDuration(option.getDuration());
        existing.setData(option.getData());
        existing.setUnit(option.getUnit());
        existing.setIsDailyPlan(option.getIsDailyPlan());
        existing.setDescription(option.getDescription());
        existing.setPrice(option.getPrice());
        existing.setCurrency(option.getCurrency());
        return optionRepository.save(existing);
    }

    @Override
    public void deleteOption(int id) {
        optionRepository.deleteById(id);
    }

    @Override
    public Optional<OptionEntity> getOptionById(int id) {
        return optionRepository.findById(id);
    }

    @Override
    public List<OptionEntity> getAllOptions() {
        return optionRepository.findAll();
    }
}
