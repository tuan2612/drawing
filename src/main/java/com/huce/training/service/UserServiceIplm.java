package com.huce.training.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.huce.training.entity.UserEntity;
import com.huce.training.repository.UserRepository;

public class UserServiceIplm implements UserService {
    @Autowired
    private UserRepository userRepo;

    @Override
    public List<UserEntity> getAllUser() {
        return userRepo.findAll();

    }
    
}
