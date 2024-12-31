package com.huce.project.service.Impl;

import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.huce.project.entity.AreaEntity;
import com.huce.project.repository.AreaRepository;
import com.huce.project.service.AreaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl implements AreaService {
    private final AreaRepository areaRepository;

    @Override
    public List<AreaEntity> getAllArea() {
        List<AreaEntity> areas = areaRepository.findAll();
        return areas;
    }

    @Override
    public Optional<AreaEntity> getAreaByParentId(Integer parentId) {

        return areaRepository.findById(parentId);
    }

}
