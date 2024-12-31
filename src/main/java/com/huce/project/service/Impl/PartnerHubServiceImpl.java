package com.huce.project.service.Impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.huce.project.entity.PartnerHubEntity;
import com.huce.project.repository.PartnerHubRepository;
import com.huce.project.service.PartnerHubService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartnerHubServiceImpl implements PartnerHubService {
    private final PartnerHubRepository partnerHubRepository;

    @Override
    @CacheEvict(cacheNames = "partnerHubCache", allEntries = true)
    public PartnerHubEntity createPartnerHub(PartnerHubEntity partnerHub) {
        return partnerHubRepository.save(partnerHub);
    }

    @Override
    @CacheEvict(cacheNames = "partnerHubCache", allEntries = true)
    public PartnerHubEntity updatePartnerHub(int id, PartnerHubEntity partnerHub) {
        PartnerHubEntity existing = partnerHubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PartnerHub not found with id: " + id));
        existing.setTitle(partnerHub.getTitle());
        existing.setDescription(partnerHub.getDescription());
        existing.setImage(partnerHub.getImage());
        return partnerHubRepository.save(existing);
    }

    @Override
    @CacheEvict(cacheNames = "partnerHubCache", allEntries = true)
    public void deletePartnerHub(int id) {
        partnerHubRepository.deleteById(id);
    }

    @Override
    @Cacheable(cacheNames = "partnerHubCache", key = "#id")
    public PartnerHubEntity getPartnerHubById(int id) {
        return partnerHubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PartnerHub not found with id: " + id));
    }

    @Override
    @Cacheable(cacheNames = "partnerHubCache", key = "'all'")
    public List<PartnerHubEntity> getAllPartnerHub() {
        return partnerHubRepository.findAll();
    }
}
