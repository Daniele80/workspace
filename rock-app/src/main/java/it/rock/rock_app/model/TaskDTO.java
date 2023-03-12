package it.rock.rock_app.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TaskDTO {

    private Long id;

    @Size(max = 255)
    private String description;

}
