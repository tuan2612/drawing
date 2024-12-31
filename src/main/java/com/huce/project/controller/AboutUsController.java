package com.huce.project.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.huce.project.dto.ResponseAPIDTO;
import com.huce.project.entity.AboutUsEntity;
import com.huce.project.service.AboutUsService;
import lombok.RequiredArgsConstructor;
import java.util.List;
@RestController
@RequestMapping("/api/v1/about-us")
@RequiredArgsConstructor
public class AboutUsController {

    private final AboutUsService aboutUsService;
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<ResponseAPIDTO<AboutUsEntity>> createAboutUs(@RequestBody AboutUsEntity aboutUs) {
        AboutUsEntity created = aboutUsService.createAboutUs(aboutUs);
        return ResponseEntity.ok(
                ResponseAPIDTO.<AboutUsEntity>builder()
                        .message("Created successfully.")
                        .result(created)
                        .build()
        );
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseAPIDTO<AboutUsEntity>> updateAboutUs(@PathVariable Integer id, @RequestBody AboutUsEntity aboutUs) {
        AboutUsEntity updated = aboutUsService.updateAboutUs(id, aboutUs);
        return ResponseEntity.ok(
                ResponseAPIDTO.<AboutUsEntity>builder()
                        .message("Updated successfully.")
                        .result(updated)
                        .build()
        );
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseAPIDTO<Void>> deleteAboutUs(@PathVariable int id) {
        aboutUsService.deleteAboutUs(id);
        return ResponseEntity.ok(
                ResponseAPIDTO.<Void>builder()
                        .message("Deleted successfully.")
                        .build()
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseAPIDTO<AboutUsEntity>> getAboutUsById(@PathVariable Integer id) {
        AboutUsEntity aboutUs = aboutUsService.getAboutUsById(id);
        return ResponseEntity.ok(
                ResponseAPIDTO.<AboutUsEntity>builder()
                        .message("Retrieved successfully.")
                        .result(aboutUs)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ResponseAPIDTO<List<AboutUsEntity>>> getAllAboutUs() {
        List<AboutUsEntity> aboutUsList = aboutUsService.getAllAboutUs();
        return ResponseEntity.ok(
                ResponseAPIDTO.<List<AboutUsEntity>>builder()
                        .message("Retrieved successfully.")
                        .result(aboutUsList)
                        .build()
        );
    }
}
