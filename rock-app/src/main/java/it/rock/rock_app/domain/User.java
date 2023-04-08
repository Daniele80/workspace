package it.rock.rock_app.domain;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "\"user\"")
public class User {



	public User(String username, String password, 
			Collection<SimpleGrantedAuthority> authorities) {
		super();
		setPassword(password);
		setUsername(username);
	}

	@Id
	@Column(nullable = false, updatable = false)
	@SequenceGenerator(
			name = "primary_sequence",
			sequenceName = "primary_sequence",
			allocationSize = 1,
			initialValue = 10000
			)
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "primary_sequence"
			)
	private Long id;

	@Column(nullable = false)
	private String username;

	@Column
	private String firstName;

	@Column
	private String lastName;

	@Column
	private String email;

	@Column
	private String password;
	@Column
	private String matchingPassword;

	@ManyToMany
	@JoinTable(
			name = "user_role",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
			)
	private Set<Role> roles;

}
