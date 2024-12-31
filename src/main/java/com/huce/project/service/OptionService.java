package com.huce.project.service;

import java.util.List;
import java.util.Optional;

import com.huce.project.entity.OptionEntity;

public interface OptionService {
    OptionEntity createOption(OptionEntity option);

    OptionEntity updateOption(int id, OptionEntity option);

    void deleteOption(int id);

    Optional<OptionEntity> getOptionById(int id);

    List<OptionEntity> getAllOptions();
}
