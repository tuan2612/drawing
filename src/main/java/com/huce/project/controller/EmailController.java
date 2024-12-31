package com.huce.project.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.huce.project.dto.ResponseAPIDTO;
import com.huce.project.entity.EsimEntity;
import com.huce.project.entity.OptionEntity;
import com.huce.project.form.getEmailForm;
import com.huce.project.service.EmailService;
import com.huce.project.service.EsimService;
import com.huce.project.service.OptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/email")
public class EmailController {
    private final EsimService esimService;
    private final EmailService emailService;
    private final OptionService optionService;

    @PostMapping("/qr")
    public ResponseEntity<ResponseAPIDTO<List<String>>> sendEmail(@RequestBody getEmailForm form) {
        try {
            List<String> successMessages = new ArrayList<>();
            List<String> errorMessages = new ArrayList<>();

            // Danh sách chứa các QR code
            List<byte[]> qrCodeImages = new ArrayList<>();
            List<String> qrCodeFileNames = new ArrayList<>();

            // Lặp qua từng cặp khóa chính
            for (getEmailForm.EsimOptionKey key : form.getEsim_option()) {
                // Retrieve the eSIM entity by ID
                Optional<EsimEntity> esimOptional = esimService.getEsimByEsimId(key.getEsim_id());
                Optional<OptionEntity> optionOptional = optionService.getOptionById(key.getOption_id());

                if (esimOptional.isPresent() && optionOptional.isPresent()) {
                    EsimEntity esim = esimOptional.get();
                    OptionEntity option = optionOptional.get();

                    byte[] qrCodeImage = esimService.generateQrCodeImage(
                            esim.getEsimName(),
                            esim.getProvider(),
                            esim.getExpiration(),
                            option.getData(),
                            option.getDuration(),
                            option.getUnit());

                    if (qrCodeImage != null) {
                        qrCodeImages.add(qrCodeImage);
                        qrCodeFileNames.add("qr-code-" + esimOptional.get().getEsimName()+ ".png");

                        successMessages.add("QR Code generated for eSIM ID: " + key.getEsim_id());
                    } else {
                        errorMessages.add("Failed to generate QR Code for eSIM ID: " + key.getEsim_id());
                    }
                } else {
                    errorMessages.add("eSIM or Option not found for ID: " + key.getEsim_id());
                }
            }

            // Gửi email với tất cả các QR code
            if (!qrCodeImages.isEmpty()) {
                emailService.sendEmailWithAttachment(
                        form.getEmail(),
                        "Your eSIM QR Codes",
                        "Please find attached QR codes for your eSIMs.",
                        qrCodeImages,
                        qrCodeFileNames,form.getPayment_id()
                        );
            }

            // Trả về response dựa trên kết quả xử lý
            if (!errorMessages.isEmpty()) {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                        .body(ResponseAPIDTO.<List<String>>builder()
                                .code(HttpStatus.PARTIAL_CONTENT.value())
                                .message("Partial processing completed")
                                .result(errorMessages)
                                .build());
            }

            return ResponseEntity.ok(
                    ResponseAPIDTO.<List<String>>builder()
                            .code(HttpStatus.OK.value())
                            .message("All QR Codes generated and sent successfully")
                            .result(successMessages)
                            .build());

        } catch (Exception e) {
            // Log và xử lý ngoại lệ
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseAPIDTO.<List<String>>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Unexpected error occurred: " + e.getMessage())
                            .build());
        }
    }
}