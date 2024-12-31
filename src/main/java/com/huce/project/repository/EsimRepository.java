package com.huce.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.huce.project.entity.EsimEntity;
import java.util.List;


public interface EsimRepository extends JpaRepository<EsimEntity, Integer> {

    boolean existsByEsimName(String esimName);

    @Query(value = "SELECT MAX(esim_id) FROM esims", nativeQuery = true)
    Integer findMaxId();
    @Query(value = "SELECT * FROM esims where esim_tag=1;", nativeQuery = true)
    List<EsimEntity> findByEsimTag();
}
