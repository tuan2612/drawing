package com.huce.project.healcheck;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@DependsOnDatabaseInitialization
@RequiredArgsConstructor
public class DatabaseHealthIndicator implements HealthIndicator {
    
    private final DataSource dataSource;
    

    @Override
    public Health health() {
        try {
            // Kiểm tra kết nối database
            if (isDatabaseHealthy()) {
                return Health.up()
                        .withDetail("database", "Database is running")
                        .build();
            } else {
                return Health.down()
                        .withDetail("database", "Database is not available")
                        .build();
            }
        } catch (Exception e) {
            return Health.down()
                    .withDetail("database", "Database health check failed")
                    .withException(e)
                    .build();
        }
    }

    private boolean isDatabaseHealthy() {
        try (Connection conn = ((Statement) dataSource).getConnection()) {
            // Thực hiện truy vấn đơn giản để kiểm tra
            try (PreparedStatement ps = conn.prepareStatement("SELECT 1")) {
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            return false;
        }
    }
}
