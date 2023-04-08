package it.rock.rock_app.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import it.rock.rock_app.domain.Project;
import it.rock.rock_app.domain.Task;


public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project findFirstByTasks(Task task);

}
