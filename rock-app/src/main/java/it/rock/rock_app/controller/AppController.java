package it.rock.rock_app.controller;

import it.rock.rock_app.domain.Project;
import it.rock.rock_app.model.AppDTO;
import it.rock.rock_app.repos.ProjectRepository;
import it.rock.rock_app.service.AppService;
import it.rock.rock_app.util.CustomCollectors;
import it.rock.rock_app.util.WebUtils;
import jakarta.validation.Valid;
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


@Controller
@RequestMapping("/apps")
public class AppController {

    private final AppService appService;
    private final ProjectRepository projectRepository;

    public AppController(final AppService appService, final ProjectRepository projectRepository) {
        this.appService = appService;
        this.projectRepository = projectRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("projectsValues", projectRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Project::getId, Project::getDescription)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("apps", appService.findAll());
        return "app/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("app") final AppDTO appDTO) {
        return "app/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("app") @Valid final AppDTO appDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "app/add";
        }
        appService.create(appDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("app.create.success"));
        return "redirect:/apps";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("app", appService.get(id));
        return "app/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("app") @Valid final AppDTO appDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "app/edit";
        }
        appService.update(id, appDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("app.update.success"));
        return "redirect:/apps";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        appService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("app.delete.success"));
        return "redirect:/apps";
    }

}
