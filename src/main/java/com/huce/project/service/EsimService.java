package com.huce.project.service;

import com.huce.project.entity.EsimEntity;
import com.huce.project.form.EsimDetailForm;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface EsimService {
    byte[] generateQrCodeImage(String plan, String provider, String esimName, int data, int duration, String unit ) throws IOException, IOException;
    // Read
    Optional<EsimEntity> getEsimByEsimId(int id);

    Optional<EsimEntity> GetEsimDetail(EsimDetailForm form);
    
    List<EsimEntity> getEsimList();
    List<EsimEntity> getAllEsimPopular();

    // Create
    EsimEntity createEsim(EsimEntity esim);

    // Update
    EsimEntity updateEsim(int esimId, EsimEntity esim);

    // Delete
    void deleteEsim(int esimId);

}
