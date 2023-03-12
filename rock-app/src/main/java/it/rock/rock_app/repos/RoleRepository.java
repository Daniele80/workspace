package it.rock.rock_app.repos;

import it.rock.rock_app.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {
}
