package com.huce.project.service.Impl;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.huce.project.entity.FAQEntity;
import com.huce.project.repository.FAQRepository;
import com.huce.project.service.FAQService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class FAQServiceImpl  implements FAQService {
    private final FAQRepository faqRepository;
    @Override
    @Cacheable(cacheNames = "faq")
    public List<FAQEntity> getAllFAQs() {
        return faqRepository.findAll();
    }
    
}
