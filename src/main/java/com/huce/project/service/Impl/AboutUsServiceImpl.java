package com.huce.project.service.Impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.huce.project.entity.AboutUsEntity;
import com.huce.project.repository.AboutUsRepository;
import com.huce.project.service.AboutUsService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AboutUsServiceImpl implements AboutUsService {

    private final AboutUsRepository aboutUsRepository;

    @Override
    @CacheEvict(cacheNames = "aboutUsCache", allEntries = true)
    public AboutUsEntity createAboutUs(AboutUsEntity aboutUs) {
        return aboutUsRepository.save(aboutUs);
    }

    @Override
    @CacheEvict(cacheNames = "aboutUsCache", allEntries = true)
    public AboutUsEntity updateAboutUs(int id, AboutUsEntity aboutUs) {
        AboutUsEntity existing = aboutUsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AboutUs not found with id: " + id));
        existing.setTitle(aboutUs.getTitle());
        existing.setDescription(aboutUs.getDescription());
        existing.setImage(aboutUs.getImage());
        return aboutUsRepository.save(existing);
    }

    @Override
    @CacheEvict(cacheNames = "aboutUsCache", allEntries = true)
    public void deleteAboutUs(int id) {
        aboutUsRepository.deleteById(id);
    }

    @Override
    @Cacheable(cacheNames = "aboutUsCache", key = "#id")
    public AboutUsEntity getAboutUsById(int id) {
        return aboutUsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AboutUs not found with id: " + id));
    }

    @Override
    @Cacheable(cacheNames = "aboutUsCache", key = "'all'")
    public List<AboutUsEntity> getAllAboutUs() {
        return aboutUsRepository.findAll();
    }
}
