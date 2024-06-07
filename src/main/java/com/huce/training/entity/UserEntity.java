package com.huce.training.entity;

import java.io.Serializable;
import org.hibernate.annotations.Formula;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "`User`")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;

	@Column(name = "username", length = 50, nullable = false, unique = true, updatable = false)
	String username;

	@Column(name = "`password`", length = 50, nullable = false)
	String password;

	@Column(name = "first_name", length = 50, nullable = false, updatable = false)
	String firstName;

	@Column(name = "last_name", length = 50, nullable = false, updatable = false)
	String lastName;

	@Formula(" concat(first_name, ' ', last_name)")
	String fullName;

	@Column(name = "`role`")
	@Enumerated(EnumType.STRING)
	RoleEntity role;

	@ManyToOne
	@JoinColumn(name = "department_id", nullable = false)
	DepartmentEntity department;

	@PrePersist
	public void prePersist() {
		if (role == null) {
			role = RoleEntity.EMPLOYEE;
		}

		if (password == null) {
			password = new BCryptPasswordEncoder().encode("123456");
		}
	}

}
