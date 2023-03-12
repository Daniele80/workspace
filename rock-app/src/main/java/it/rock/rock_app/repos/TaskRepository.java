package it.rock.rock_app.repos;

import it.rock.rock_app.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskRepository extends JpaRepository<Task, Long> {
}
