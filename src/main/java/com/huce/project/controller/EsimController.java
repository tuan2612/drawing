package com.huce.project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huce.project.dto.CRUDEsimDTO;
import com.huce.project.dto.EsimDTO;
import com.huce.project.dto.ResponseAPIDTO;
import com.huce.project.entity.EsimEntity;
import com.huce.project.form.EsimDetailForm;
import com.huce.project.service.EsimService;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;;

@RestController
@RequestMapping("api/v1/esim")
@RequiredArgsConstructor

public class EsimController {
    private final EsimService esimService;
    private final ModelMapper modelMapper;

    @PostMapping("/getEsimDetail")
    public ResponseEntity<ResponseAPIDTO<EsimDTO>> getEsimDetail(@RequestBody EsimDetailForm form) {
        Optional<EsimEntity> esimEntity = esimService.GetEsimDetail(form);

        if (esimEntity.isEmpty()) { // Check if esimEntity is empty
            ResponseAPIDTO<EsimDTO> response = ResponseAPIDTO.<EsimDTO>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message("eSIM not found")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        EsimDTO esimDTO = modelMapper.map(esimEntity.get(), EsimDTO.class);

        // Build successful response
        ResponseAPIDTO<EsimDTO> response = ResponseAPIDTO.<EsimDTO>builder()
                .code(HttpStatus.OK.value())
                .message("eSIM found")
                .result(esimDTO)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/addEsim")
    public ResponseEntity<ResponseAPIDTO<CRUDEsimDTO>> addEsim(@RequestBody CRUDEsimDTO form) {

        // Validate input
        if (form.getEsimName() == null || form.getEsimName().trim().isEmpty()) {
            ResponseAPIDTO<CRUDEsimDTO> response = ResponseAPIDTO.<CRUDEsimDTO>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("eSIM name cannot be empty")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Convert DTO to Entity
        EsimEntity esim = modelMapper.map(form, EsimEntity.class);

        try {
            esimService.createEsim(esim);
            ResponseAPIDTO<CRUDEsimDTO> response = ResponseAPIDTO.<CRUDEsimDTO>builder()
                    .code(HttpStatus.OK.value())
                    .message("eSIM created successfully")
                    .result(form)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (IllegalArgumentException e) {

            ResponseAPIDTO<CRUDEsimDTO> response = ResponseAPIDTO.<CRUDEsimDTO>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/updateEsim/{esimId}")
    public ResponseEntity<ResponseAPIDTO<CRUDEsimDTO>> updateEsim(@PathVariable int esimId,
            @RequestBody CRUDEsimDTO form) {

        // Convert DTO to Entity
        EsimEntity esim = modelMapper.map(form, EsimEntity.class);
        esim.setEsimId(esimId);
        try {
            // Update eSIM through service
            esimService.updateEsim(esimId, esim);
            // Create success response
            ResponseAPIDTO<CRUDEsimDTO> response = ResponseAPIDTO.<CRUDEsimDTO>builder()
                    .code(HttpStatus.OK.value())
                    .message("eSIM updated successfully")
                    .result(form)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (IllegalArgumentException e) {

            ResponseAPIDTO<CRUDEsimDTO> response = ResponseAPIDTO.<CRUDEsimDTO>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/deleteEsim/{esimId}")
    public ResponseEntity<ResponseAPIDTO<String>> deleteEsim(@PathVariable int esimId) {
        try {
            esimService.deleteEsim(esimId);

            ResponseAPIDTO<String> response = ResponseAPIDTO.<String>builder()
                    .code(HttpStatus.OK.value())
                    .message("eSIM deleted successfully")
                    .result(null)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (RuntimeException e) {

            ResponseAPIDTO<String> response = ResponseAPIDTO.<String>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .result(null)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        }
    }

    @GetMapping("getEsims")
    public ResponseEntity<ResponseAPIDTO<List<CRUDEsimDTO>>> getAllEsims() {

        List<EsimEntity> esimEntities = esimService.getEsimList();
        // Check if the result is empty or null
        if (esimEntities == null || esimEntities.isEmpty()) {
            ResponseAPIDTO<List<CRUDEsimDTO>> response = ResponseAPIDTO.<List<CRUDEsimDTO>>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message("No eSIMs found")
                    .result(Collections.emptyList())
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        // Convert List<EsimEntity> to List<CRUDEsimDTO>
        List<CRUDEsimDTO> crudeEsimDTOs = esimEntities.stream()
                .map(esim -> modelMapper.map(esim, CRUDEsimDTO.class))
                .collect(Collectors.toList());

        ResponseAPIDTO<List<CRUDEsimDTO>> response = ResponseAPIDTO.<List<CRUDEsimDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("eSIMs found")
                .result(crudeEsimDTOs)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("getEsimPopular")
    public ResponseEntity<ResponseAPIDTO<List<CRUDEsimDTO>>> getAllEsimPopular() {

        List<EsimEntity> esimEntities = esimService.getAllEsimPopular();

        // Check if the result is empty or null
        if (esimEntities == null || esimEntities.isEmpty()) {
            ResponseAPIDTO<List<CRUDEsimDTO>> response = ResponseAPIDTO.<List<CRUDEsimDTO>>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message("No eSIMs found")
                    .result(Collections.emptyList())
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        // Convert List<EsimEntity> to List<CRUDEsimDTO>
        List<CRUDEsimDTO> crudeEsimDTOs = esimEntities.stream()
                .map(esim -> modelMapper.map(esim, CRUDEsimDTO.class))
                .collect(Collectors.toList());

        ResponseAPIDTO<List<CRUDEsimDTO>> response = ResponseAPIDTO.<List<CRUDEsimDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("eSIMs found")
                .result(crudeEsimDTOs)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
