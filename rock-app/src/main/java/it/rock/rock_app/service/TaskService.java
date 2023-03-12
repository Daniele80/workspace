package it.rock.rock_app.service;

import it.rock.rock_app.domain.Project;
import it.rock.rock_app.domain.Task;
import it.rock.rock_app.model.TaskDTO;
import it.rock.rock_app.repos.ProjectRepository;
import it.rock.rock_app.repos.TaskRepository;
import it.rock.rock_app.util.NotFoundException;
import it.rock.rock_app.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public TaskService(final TaskRepository taskRepository,
            final ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public List<TaskDTO> findAll() {
        final List<Task> tasks = taskRepository.findAll(Sort.by("id"));
        return tasks.stream()
                .map((task) -> mapToDTO(task, new TaskDTO()))
                .toList();
    }

    public TaskDTO get(final Long id) {
        return taskRepository.findById(id)
                .map(task -> mapToDTO(task, new TaskDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final TaskDTO taskDTO) {
        final Task task = new Task();
        mapToEntity(taskDTO, task);
        return taskRepository.save(task).getId();
    }

    public void update(final Long id, final TaskDTO taskDTO) {
        final Task task = taskRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(taskDTO, task);
        taskRepository.save(task);
    }

    public void delete(final Long id) {
        taskRepository.deleteById(id);
    }

    private TaskDTO mapToDTO(final Task task, final TaskDTO taskDTO) {
        taskDTO.setId(task.getId());
        taskDTO.setDescription(task.getDescription());
        return taskDTO;
    }

    private Task mapToEntity(final TaskDTO taskDTO, final Task task) {
        task.setDescription(taskDTO.getDescription());
        return task;
    }

    public String getReferencedWarning(final Long id) {
        final Task task = taskRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Project tasksProject = projectRepository.findFirstByTasks(task);
        if (tasksProject != null) {
            return WebUtils.getMessage("task.project.tasks.referenced", tasksProject.getId());
        }
        return null;
    }

}
