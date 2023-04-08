package it.rock.rock_app.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import it.rock.rock_app.domain.App;
import it.rock.rock_app.domain.Project;
import it.rock.rock_app.domain.Task;
import it.rock.rock_app.model.ProjectDTO;
import it.rock.rock_app.repos.AppRepository;
import it.rock.rock_app.repos.ProjectRepository;
import it.rock.rock_app.repos.TaskRepository;
import it.rock.rock_app.util.NotFoundException;
import it.rock.rock_app.util.WebUtils;
import jakarta.transaction.Transactional;


@Transactional
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final AppRepository appRepository;

    public ProjectService(final ProjectRepository projectRepository,
            final TaskRepository taskRepository, final AppRepository appRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.appRepository = appRepository;
    }

    public List<ProjectDTO> findAll() {
        final List<Project> projects = projectRepository.findAll(Sort.by("id"));
        return projects.stream()
                .map((project) -> mapToDTO(project, new ProjectDTO()))
                .toList();
    }

    public ProjectDTO get(final Long id) {
        return projectRepository.findById(id)
                .map(project -> mapToDTO(project, new ProjectDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ProjectDTO projectDTO) {
        final Project project = new Project();
        mapToEntity(projectDTO, project);
        return projectRepository.save(project).getId();
    }

    public void update(final Long id, final ProjectDTO projectDTO) {
        final Project project = projectRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(projectDTO, project);
        projectRepository.save(project);
    }

    public void delete(final Long id) {
        projectRepository.deleteById(id);
    }

    private ProjectDTO mapToDTO(final Project project, final ProjectDTO projectDTO) {
        projectDTO.setId(project.getId());
        projectDTO.setDescription(project.getDescription());
        projectDTO.setTasks(project.getTasks() == null ? null : project.getTasks().stream()
                .map(task -> task.getId())
                .toList());
        return projectDTO;
    }

    private Project mapToEntity(final ProjectDTO projectDTO, final Project project) {
        project.setDescription(projectDTO.getDescription());
        final List<Task> tasks = taskRepository.findAllById(
                projectDTO.getTasks() == null ? Collections.emptyList() : projectDTO.getTasks());
        if (tasks.size() != (projectDTO.getTasks() == null ? 0 : projectDTO.getTasks().size())) {
            throw new NotFoundException("one of tasks not found");
        }
        project.setTasks(tasks.stream().collect(Collectors.toSet()));
        return project;
    }

    public String getReferencedWarning(final Long id) {
        final Project project = projectRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final App projectsApp = appRepository.findFirstByProjects(project);
        if (projectsApp != null) {
            return WebUtils.getMessage("project.app.projects.referenced", projectsApp.getId());
        }
        return null;
    }

}
