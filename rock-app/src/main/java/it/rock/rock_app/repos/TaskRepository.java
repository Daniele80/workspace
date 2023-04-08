package it.rock.rock_app.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import it.rock.rock_app.domain.Task;


public interface TaskRepository extends JpaRepository<Task, Long> {
}
