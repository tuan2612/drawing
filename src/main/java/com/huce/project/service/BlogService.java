package com.huce.project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.huce.project.entity.BlogEntity;

public interface BlogService {
    public BlogEntity getBlogById(int id);

    public BlogEntity addBlog(BlogEntity form);

    public BlogEntity updateBlog(int id, BlogEntity form);

    public void deleteBlog(int id);

    public Page<BlogEntity>findAll(Pageable pageable);


}
