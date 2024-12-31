package com.huce.project.controller;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huce.project.dto.FAQDTO;
import com.huce.project.dto.ResponseAPIDTO;
import com.huce.project.entity.FAQEntity;
import com.huce.project.service.FAQService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/v1/faq")
@RequiredArgsConstructor
public class FAQController {
    private final FAQService faqService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<ResponseAPIDTO<List<FAQDTO>>> getAreas() {
        try {
            List<FAQEntity> faqEntities = faqService.getAllFAQs();

            if (faqEntities.isEmpty()) {
                return ResponseEntity.ok(ResponseAPIDTO.<List<FAQDTO>>builder()
                        .code(HttpStatus.OK.value())
                        .message("No FAQ found")
                        .result(Collections.emptyList())
                        .build());
            }

            Type listType = new TypeToken<List<FAQDTO>>() {
            }.getType();
            List<FAQDTO> mappedAreas = modelMapper.map(faqEntities, listType);

            return ResponseEntity.ok(ResponseAPIDTO.<List<FAQDTO>>builder()
                    .code(HttpStatus.OK.value())
                    .message("FAQ retrieved successfully")
                    .result(mappedAreas)
                    .build());
        } catch (Exception e) {
       
            e.printStackTrace();

            // Return a response with an error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseAPIDTO.<List<FAQDTO>>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred while retrieving FAQs")
                            .result(null)
                            .build());
        }
    }
}
