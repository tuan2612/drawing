package com.huce.project.service;

import java.util.List;

import com.huce.project.entity.AboutUsEntity;

public interface AboutUsService {
    AboutUsEntity createAboutUs(AboutUsEntity aboutUs);

    AboutUsEntity updateAboutUs(int id, AboutUsEntity aboutUs);

    void deleteAboutUs(int id);

    AboutUsEntity getAboutUsById(int id);

    List<AboutUsEntity> getAllAboutUs();
}
