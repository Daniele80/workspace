package it.rock.rock_app.model;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {

  private Long id;

    @NotNull
    @Size(max = 255)
    private String username;

    @Size(max = 255)
    @NotEmpty(message = "Password can not be empty")
    private String password;

    @Size(max = 255)
    private String matchingPassword;

    private String firstName;
    
    private String lastName;
       
    private String email;
   
    private List<Long> roles;
}
