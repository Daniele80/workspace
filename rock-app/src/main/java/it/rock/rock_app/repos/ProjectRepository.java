package it.rock.rock_app.repos;

import it.rock.rock_app.domain.Project;
import it.rock.rock_app.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project findFirstByTasks(Task task);

}
