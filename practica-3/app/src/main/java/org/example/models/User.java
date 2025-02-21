package org.example.models;

import jakarta.persistence.*;
import lombok.*;
import org.example.util.AccessStatus;
import org.example.util.Role;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String name;
	private String password;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Enumerated(EnumType.STRING)
	private AccessStatus accessStatus;
}
