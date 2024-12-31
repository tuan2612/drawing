package com.huce.project.service;

import java.util.List;
import java.util.Optional;

import com.huce.project.entity.AreaEntity;


public interface AreaService {
    public List<AreaEntity> getAllArea();
    public Optional<AreaEntity> getAreaByParentId(Integer parentId);
} 