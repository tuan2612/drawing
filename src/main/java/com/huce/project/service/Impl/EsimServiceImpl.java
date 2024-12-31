package com.huce.project.service.Impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.huce.project.entity.EsimEntity;
import com.huce.project.entity.EsimOption;
import com.huce.project.entity.OptionEntity;
import com.huce.project.form.EsimDetailForm;
import com.huce.project.repository.CountryRepository;
import com.huce.project.repository.EsimRepository;
import com.huce.project.service.EsimService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = { "esimCache" })
public class EsimServiceImpl implements EsimService {

    private static final Logger logger = LoggerFactory.getLogger(EsimServiceImpl.class);

    private final EsimRepository esimRepository;
    private final CountryRepository countryRepository;
    private final Double RATE = 0.85D;

    @Override
    @Cacheable(key = "#form.esim_id", condition = "#form.currency == 'EUR'")
    public Optional<EsimEntity> GetEsimDetail(EsimDetailForm form) {
        Optional<EsimEntity> esimOpt = esimRepository.findById(form.getEsim_id());

        if (esimOpt.isPresent()) {
            EsimEntity esim = esimOpt.get();

            // Process currency conversion for each option
            if (esim.getEsimOptions() != null) {
                for (EsimOption esimOption : esim.getEsimOptions()) {
                    OptionEntity option = esimOption.getOption();
                    if (option != null) {
                        BigDecimal originalPrice = option.getPrice();

                        if (form.getCurrency().equalsIgnoreCase("EUR")) {
                            BigDecimal convertedPrice = originalPrice.multiply(BigDecimal.valueOf(RATE));
                            option.setPrice(convertedPrice);
                            option.setCurrency("EUR");
                        }
                    }
                }
            }
        }

        return esimOpt;
    }

    private int qrCodeSize;

    public byte[] generateQrCodeImage(String plan, String provider, String esimName, int data, int duration,
            String unit) throws IOException {
        String qrData = "Plan: %s\nProvider: %s\nEsimName: %s\nData: %s\nDuration: %s\nUnit: %s"
                .formatted(plan, provider, esimName, data, duration, unit);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize);
            BufferedImage qrImage = new BufferedImage(bitMatrix.getWidth(), bitMatrix.getHeight(),
                    BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < bitMatrix.getWidth(); x++) {
                for (int y = 0; y < bitMatrix.getHeight(); y++) {
                    qrImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", baos);
            return baos.toByteArray();

        } catch (WriterException e) {
            logger.error("Failed to generate QR code", e);
            throw new IOException("Failed to generate QR code", e);
        }
    }

    @Override
    public Optional<EsimEntity> getEsimByEsimId(int id) {
        return esimRepository.findById(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public EsimEntity createEsim(EsimEntity esim) {
        // Validate esim name
        if (esim.getEsimName() == null || esim.getEsimName().trim().isEmpty()) {
            throw new IllegalArgumentException("eSIM name cannot be empty");
        }
        if (esim.getCountryEntity() == null ||
                !countryRepository.existsByCountryId(esim.getCountryEntity().getCountryId())) {
            throw new IllegalArgumentException("Country ID not found");
        }
        // Check duplicate esim name
        if (esimRepository.existsByEsimName(esim.getEsimName())) {
            throw new IllegalArgumentException("eSIM name already exists");
        }

        Integer maxId = esimRepository.findMaxId();
        int nextId = maxId + 1;
        esim.setEsimId(nextId);

        return esimRepository.save(esim);
    }

    @Override
    @CacheEvict(value = "esims", allEntries = true) 
    public EsimEntity updateEsim(int esimId, EsimEntity updatedEsim) {
 
        EsimEntity existingEsim = esimRepository.findById(esimId)
                .orElseThrow(() -> new RuntimeException("eSIM not found with id: " + esimId));

        if (updatedEsim.getEsimName() != null) {
            existingEsim.setEsimName(updatedEsim.getEsimName());
        }

        if (updatedEsim.getCountryEntity() != null && updatedEsim.getCountryEntity().getCountryId() != 0) {
            existingEsim.setCountryEntity(updatedEsim.getCountryEntity());
        }

        if (updatedEsim.getEsimTag() != null) {
            existingEsim.setEsimTag(updatedEsim.getEsimTag());
        }
        if (updatedEsim.getPolicy() != null) {
            existingEsim.setPolicy(updatedEsim.getPolicy());
        }
        if (updatedEsim.getHotspot() != null) {
            existingEsim.setHotspot(updatedEsim.getHotspot());
        }
        if (updatedEsim.getVoiceCallsSms() != null) {
            existingEsim.setVoiceCallsSms(updatedEsim.getVoiceCallsSms());
        }
        if (updatedEsim.getDescription() != null) {
            existingEsim.setDescription(updatedEsim.getDescription());
        }
        if (updatedEsim.getProvider() != null) {
            existingEsim.setProvider(updatedEsim.getProvider());
        }
        if (updatedEsim.getEsimImage() != null) {
            existingEsim.setEsimImage(updatedEsim.getEsimImage());
        }

        // Lưu thực thể đã cập nhật
        return esimRepository.save(existingEsim);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void deleteEsim(int esimId) {
        if (!esimRepository.existsById(esimId)) {
            throw new RuntimeException("eSIM not found with id: " + esimId);
        }
        esimRepository.deleteById(esimId);
    }

    @Override
    @CacheEvict(allEntries = true)
    public List<EsimEntity> getEsimList() {
        return esimRepository.findAll();
    }

    @Override
    public List<EsimEntity> getAllEsimPopular() {
        return esimRepository.findByEsimTag();
    }
}