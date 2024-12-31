package com.huce.project.controller;

import org.modelmapper.ModelMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huce.project.config.jwtConfigs;
import com.huce.project.dto.AdminDTO;
import com.huce.project.dto.JwtResponseDTO;
import com.huce.project.dto.ResponseAPIDTO;
import com.huce.project.entity.AdminEntity;
import com.huce.project.repository.AdminRepository;
import com.huce.project.service.Impl.AdminDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final jwtConfigs jwtUtils;

    private final AdminRepository adRepository;

    private final ModelMapper modelMapper;

    @PostMapping("/signin")
    public ResponseEntity<ResponseAPIDTO<?>> authenticateUser(@RequestBody @Valid AdminDTO lgDTO) {
        try {
            // Xác thực tài khoản và mật khẩu
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(lgDTO.getUsername(), lgDTO.getPassword()));

            // Lưu thông tin vào SecurityContext
            SecurityContextHolder.getContext().setAuthentication(auth);

            // Tạo JWT token
            String jwt = jwtUtils.generateJwtToken(auth);

            AdminDetailsImpl userDetails = (AdminDetailsImpl) auth.getPrincipal();

            JwtResponseDTO response = new JwtResponseDTO(jwt, userDetails.getUsername());

            return ResponseEntity.ok(
                    ResponseAPIDTO.<JwtResponseDTO>builder()
                            .code(200)
                            .message("signin successful")
                            .result(response)
                            .build());

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ResponseAPIDTO.builder()
                            .code(HttpStatus.UNAUTHORIZED.value())
                            .message("account or password is incorrect")
                            .build());
        }
    }


    // @Transactional
    @PostMapping("/signup")

    public ResponseEntity<?> registerUser(@RequestBody AdminDTO signupDTO) {
        if (adRepository.existsByUsername(signupDTO.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        AdminEntity ac = modelMapper.map(signupDTO, AdminEntity.class);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String enCryptPassword = passwordEncoder.encode(signupDTO.getPassword());
        ac.setPassword(enCryptPassword);
        adRepository.save(ac);
        return ResponseEntity.ok().body("User registered successfully!");
    }

}
