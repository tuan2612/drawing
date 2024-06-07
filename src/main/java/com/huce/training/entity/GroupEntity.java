package com.huce.training.entity;

import java.io.Serializable;
import java.security.Timestamp;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String groupName;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    Timestamp create_date;
}
