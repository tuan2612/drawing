package com.huce.project.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.huce.project.form.OptionForm;
import com.huce.project.service.OptionService;

import lombok.RequiredArgsConstructor;

import com.huce.project.entity.OptionEntity;
import com.huce.project.dto.ResponseAPIDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/options")
@RequiredArgsConstructor
public class OptionController {

    private OptionService optionService;

    private final ModelMapper modelMapper;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<ResponseAPIDTO<OptionEntity>> createOption(@RequestBody OptionForm optionForm) {
        OptionEntity option = modelMapper.map(optionForm, OptionEntity.class);
        OptionEntity created = optionService.createOption(option);
        return ResponseEntity.ok(
                ResponseAPIDTO.<OptionEntity>builder()
                        .message("Option created successfully.")
                        .result(created)
                        .build());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    // Update Option
    @PutMapping("/{id}")
    public ResponseEntity<ResponseAPIDTO<OptionEntity>> updateOption(@PathVariable int id,
            @RequestBody OptionForm optionForm) {
        OptionEntity option = modelMapper.map(optionForm, OptionEntity.class);
        OptionEntity updated = optionService.updateOption(id, option);
        return ResponseEntity.ok(
                ResponseAPIDTO.<OptionEntity>builder()
                        .message("Option updated successfully.")
                        .result(updated)
                        .build());
    }

    // Get Option by ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseAPIDTO<OptionEntity>> getOptionById(@PathVariable int id) {
        Optional<OptionEntity> option = optionService.getOptionById(id);
        return ResponseEntity.ok(
                ResponseAPIDTO.<OptionEntity>builder()
                        .message("Option retrieved successfully.")
                        .result(option.get())
                        .build());
    }

    // Get All Options
    @GetMapping
    public ResponseEntity<ResponseAPIDTO<List<OptionForm>>> getAllOptions() {
        List<OptionForm> options = optionService.getAllOptions().stream()
                .map(option -> modelMapper.map(option, OptionForm.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(
                ResponseAPIDTO.<List<OptionForm>>builder()
                        .message("Options retrieved successfully.")
                        .result(options)
                        .build());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    // Delete Option
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseAPIDTO<Void>> deleteOption(@PathVariable int id) {
        optionService.deleteOption(id);
        return ResponseEntity.ok(
                ResponseAPIDTO.<Void>builder()
                        .message("Option deleted successfully.")
                        .build());
    }
}
