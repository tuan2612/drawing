package com.huce.project.dto;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponseDTO {
    @NonNull
    private String token;
    @NonNull
    private String username;
}
