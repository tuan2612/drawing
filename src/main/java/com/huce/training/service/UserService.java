package com.huce.training.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.huce.training.entity.UserEntity;

@Service
public interface UserService {
    List<UserEntity> getAllUser();
}
