package com.huce.project.repository;

import com.huce.project.entity.EsimOption;
import com.huce.project.entity.ComposeKeyEsimOption;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EsimOptionRepository extends JpaRepository<EsimOption, ComposeKeyEsimOption> {
    Optional<EsimOption> findById(ComposeKeyEsimOption compositeKey);
}
