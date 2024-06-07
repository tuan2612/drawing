package com.huce.training.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.huce.training.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{
    List<UserEntity> findById(long id);
} 
