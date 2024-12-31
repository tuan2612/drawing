package com.huce.project.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.huce.project.entity.PartnerHubEntity;
@Repository
public interface PartnerHubRepository extends JpaRepository<PartnerHubEntity, Integer> {

}
