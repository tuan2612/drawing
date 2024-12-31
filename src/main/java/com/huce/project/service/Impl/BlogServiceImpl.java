package com.huce.project.service.Impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.huce.project.entity.BlogEntity;
import com.huce.project.repository.BlogRepository;
import com.huce.project.service.BlogService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;

    @Override
    @Cacheable(cacheNames = "blogs", key = "#id")
    public BlogEntity getBlogById(int id) {
        return blogRepository.findById(id).orElse(null);
    }


    @Override
    @CacheEvict(cacheNames = "blogs", allEntries = true)
    public BlogEntity addBlog(BlogEntity form) {
        return blogRepository.save(form);
    }

    @Override
    @CacheEvict(cacheNames = "blogs", allEntries = true)
    public BlogEntity updateBlog(int id, BlogEntity form) {
        return blogRepository.save(form);
    }

    @Override
    @CacheEvict(cacheNames = "blogs", allEntries = true)
    public void deleteBlog(int id) {
        blogRepository.deleteById(id);
    }

    @Override
    public Page<BlogEntity> findAll(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }
}
