package it.rock.rock_app.model;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthRequest {
    @NotNull @Length(min = 5, max = 50)
    private String username;
     
    @NotNull @Length(min = 5, max = 10)
    private String password;
     
}
