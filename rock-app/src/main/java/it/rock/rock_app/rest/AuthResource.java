package it.rock.rock_app.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import it.rock.rock_app.model.UserDTO;
import it.rock.rock_app.service.UserService;
import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthResource {

    private final UserService userService;

    public AuthResource(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createUser(@RequestBody @Valid final UserDTO userDTO) {
        return new ResponseEntity<>(userService.create(userDTO), HttpStatus.CREATED);
    }
    
    @PostMapping("/login")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UserDTO> login(@RequestBody @Valid final UserDTO userDTO) {
        return new ResponseEntity<>(userService.findByUsernameAndPassword(userDTO.getUsername(),
        		userDTO.getPassword()), HttpStatus.CREATED);
    }

}
