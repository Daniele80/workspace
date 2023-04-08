package it.rock.rock_app.controller;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.rock.rock_app.domain.Task;
import it.rock.rock_app.model.ProjectDTO;
import it.rock.rock_app.repos.TaskRepository;
import it.rock.rock_app.service.ProjectService;
import it.rock.rock_app.util.CustomCollectors;
import it.rock.rock_app.util.WebUtils;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final TaskRepository taskRepository;

    public ProjectController(final ProjectService projectService,
            final TaskRepository taskRepository) {
        this.projectService = projectService;
        this.taskRepository = taskRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("tasksValues", taskRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Task::getId, Task::getDescription)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("projects", projectService.findAll());
        return "project/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("project") final ProjectDTO projectDTO) {
        return "project/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("project") @Valid final ProjectDTO projectDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "project/add";
        }
        projectService.create(projectDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("project.create.success"));
        return "redirect:/projects";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("project", projectService.get(id));
        return "project/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("project") @Valid final ProjectDTO projectDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "project/edit";
        }
        projectService.update(id, projectDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("project.update.success"));
        return "redirect:/projects";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = projectService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            projectService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("project.delete.success"));
        }
        return "redirect:/projects";
    }

}
