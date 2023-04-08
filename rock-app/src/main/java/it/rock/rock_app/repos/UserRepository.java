package it.rock.rock_app.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.rock.rock_app.domain.Role;
import it.rock.rock_app.domain.User;


public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByRoles(Role role);
    User findByUsername(String username);
    Optional<User> findByEmail(String email);
    User findByUsernameAndPassword(String username, String password);

}
