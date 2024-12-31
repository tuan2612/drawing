package com.huce.project.service;

import java.util.List;

import com.huce.project.entity.PartnerHubEntity;

public interface PartnerHubService {
    PartnerHubEntity createPartnerHub(PartnerHubEntity partnerHub);

    PartnerHubEntity updatePartnerHub(int id, PartnerHubEntity partnerHub);

    void deletePartnerHub(int id);

    PartnerHubEntity getPartnerHubById(int id);

    List<PartnerHubEntity> getAllPartnerHub();
}
