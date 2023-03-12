package it.rock.rock_app.service;

import it.rock.rock_app.domain.App;
import it.rock.rock_app.domain.Project;
import it.rock.rock_app.model.AppDTO;
import it.rock.rock_app.repos.AppRepository;
import it.rock.rock_app.repos.ProjectRepository;
import it.rock.rock_app.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Transactional
@Service
public class AppService {

    private final AppRepository appRepository;
    private final ProjectRepository projectRepository;

    public AppService(final AppRepository appRepository,
            final ProjectRepository projectRepository) {
        this.appRepository = appRepository;
        this.projectRepository = projectRepository;
    }

    public List<AppDTO> findAll() {
        final List<App> apps = appRepository.findAll(Sort.by("id"));
        return apps.stream()
                .map((app) -> mapToDTO(app, new AppDTO()))
                .toList();
    }

    public AppDTO get(final Long id) {
        return appRepository.findById(id)
                .map(app -> mapToDTO(app, new AppDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final AppDTO appDTO) {
        final App app = new App();
        mapToEntity(appDTO, app);
        return appRepository.save(app).getId();
    }

    public void update(final Long id, final AppDTO appDTO) {
        final App app = appRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(appDTO, app);
        appRepository.save(app);
    }

    public void delete(final Long id) {
        appRepository.deleteById(id);
    }

    private AppDTO mapToDTO(final App app, final AppDTO appDTO) {
        appDTO.setId(app.getId());
        appDTO.setDescription(app.getDescription());
        appDTO.setProjects(app.getProjects() == null ? null : app.getProjects().stream()
                .map(project -> project.getId())
                .toList());
        return appDTO;
    }

    private App mapToEntity(final AppDTO appDTO, final App app) {
        app.setDescription(appDTO.getDescription());
        final List<Project> projects = projectRepository.findAllById(
                appDTO.getProjects() == null ? Collections.emptyList() : appDTO.getProjects());
        if (projects.size() != (appDTO.getProjects() == null ? 0 : appDTO.getProjects().size())) {
            throw new NotFoundException("one of projects not found");
        }
        app.setProjects(projects.stream().collect(Collectors.toSet()));
        return app;
    }

}
