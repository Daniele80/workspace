package it.rock.rock_app.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import it.rock.rock_app.domain.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Role findByName(String name);
}
