package it.rock.rock_app.repos;

import it.rock.rock_app.domain.Role;
import it.rock.rock_app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByRoles(Role role);
    User findByEmail(String email);

}
