package com.huce.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.huce.project.dto.ResponseAPIDTO;
import com.huce.project.entity.PartnerHubEntity;
import com.huce.project.service.PartnerHubService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/partner-hub")
public class PartnerHubController {

    @Autowired
    private PartnerHubService partnerHubService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<ResponseAPIDTO<PartnerHubEntity>> createPartnerHub(@RequestBody PartnerHubEntity partnerHub) {
        PartnerHubEntity created = partnerHubService.createPartnerHub(partnerHub);
        return ResponseEntity.ok(
                ResponseAPIDTO.<PartnerHubEntity>builder()
                        .message("Created successfully.")
                        .result(created)
                        .build());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseAPIDTO<PartnerHubEntity>> updatePartnerHub(@PathVariable int id,
            @RequestBody PartnerHubEntity partnerHub) {
        PartnerHubEntity updated = partnerHubService.updatePartnerHub(id, partnerHub);
        return ResponseEntity.ok(
                ResponseAPIDTO.<PartnerHubEntity>builder()
                        .message("Updated successfully.")
                        .result(updated)
                        .build());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseAPIDTO<Void>> deletePartnerHub(@PathVariable int id) {
        partnerHubService.deletePartnerHub(id);
        return ResponseEntity.ok(
                ResponseAPIDTO.<Void>builder()
                        .message("Deleted successfully.")
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseAPIDTO<PartnerHubEntity>> getPartnerHubById(@PathVariable int id) {
        PartnerHubEntity partnerHub = partnerHubService.getPartnerHubById(id);
        return ResponseEntity.ok(
                ResponseAPIDTO.<PartnerHubEntity>builder()
                        .message("Retrieved successfully.")
                        .result(partnerHub)
                        .build());
    }
    @GetMapping
    public ResponseEntity<ResponseAPIDTO<List<PartnerHubEntity>>> getAllPartnerHub() {
        List<PartnerHubEntity> partnerHubList = partnerHubService.getAllPartnerHub();
        return ResponseEntity.ok(
                ResponseAPIDTO.<List<PartnerHubEntity>>builder()
                        .message("Retrieved successfully.")
                        .result(partnerHubList)
                        .build());
    }
}
