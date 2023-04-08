package it.rock.rock_app.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import it.rock.rock_app.domain.App;
import it.rock.rock_app.domain.Project;


public interface AppRepository extends JpaRepository<App, Long> {

    App findFirstByProjects(Project project);

}
