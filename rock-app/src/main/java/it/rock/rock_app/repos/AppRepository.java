package it.rock.rock_app.repos;

import it.rock.rock_app.domain.App;
import it.rock.rock_app.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AppRepository extends JpaRepository<App, Long> {

    App findFirstByProjects(Project project);

}
