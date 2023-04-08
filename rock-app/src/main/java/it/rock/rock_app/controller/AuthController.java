package it.rock.rock_app.controller;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.rock.rock_app.domain.Role;
import it.rock.rock_app.model.UserDTO;
import it.rock.rock_app.repos.RoleRepository;
import it.rock.rock_app.service.UserService;
import it.rock.rock_app.util.CustomCollectors;
import it.rock.rock_app.util.UserAlreadyExistException;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/auth")
public class AuthController {
    public static final String REDIRECT="redirect:";

    private final UserService userService;
    private final RoleRepository roleRepository;

    public AuthController(final UserService userService, final RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("rolesValues", roleRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Role::getId, Role::getName)));
    }

    @GetMapping("/register")
    public String register(final Model model){
        model.addAttribute("user", new UserDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String userRegistration(final @Valid  UserDTO userData, final BindingResult bindingResult, final Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("registrationForm", userData);
            return "auth/register";
        }
        try {
            userService.register(userData);
        }catch (UserAlreadyExistException e){
            bindingResult.rejectValue("email", "userData.email","An account already exists for this email.");
            model.addAttribute("registrationForm", userData);
            return "home/index";
        }
        return "home/index";
    }
    
    @GetMapping("/login")
    public String login(final Model model){
        model.addAttribute("user", new UserDTO());
        return "auth/login";
    }
    
    @PostMapping("/login")
    public String userLogin(final @Valid  UserDTO userData, final BindingResult bindingResult, final Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("loginForm", userData);
            return "auth/login";
        }
        try {
        	userService.findByUsernameAndPassword(userData.getUsername(), userData.getPassword());
        }catch (Exception e){
            bindingResult.rejectValue("email", "userData.email","An account already exists for this email.");
            model.addAttribute("loginForm", userData);
            return "home/index";
        }
        return "home/index";
    }

}
