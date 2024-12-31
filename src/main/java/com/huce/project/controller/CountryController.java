package com.huce.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huce.project.dto.CountryDTO;
import com.huce.project.dto.ResponseAPIDTO;
import com.huce.project.entity.CountryEntity;
import com.huce.project.form.SearchForm;
import com.huce.project.service.CountryService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

@RestController
@RequestMapping("/api/v1/country")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<ResponseAPIDTO<List<CountryDTO>>> getAllCountry(SearchForm form) {
        try {
            // Retrieve country entities based on the search form
            List<CountryEntity> countryEntities = countryService.getAllCountry(form);

            // Map entities to DTOs
            List<CountryDTO> countryDTOs = modelMapper.map(countryEntities, new TypeToken<List<CountryDTO>>() {
            }.getType());

            // Build the success response
            ResponseAPIDTO<List<CountryDTO>> response = ResponseAPIDTO.<List<CountryDTO>>builder()
                    .code(HttpStatus.OK.value())
                    .message("Success")
                    .result(countryDTOs)
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Log the error (optional, replace with proper logging)
            e.printStackTrace();

            // Build the error response
            ResponseAPIDTO<List<CountryDTO>> errorResponse = ResponseAPIDTO.<List<CountryDTO>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("An error occurred while retrieving countries: " + e.getMessage())
                    .result(null)
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
