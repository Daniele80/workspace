package it.rock.rock_app.model;

import java.util.List;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProjectDTO {

    private Long id;

    @Size(max = 255)
    private String description;

    private List<Long> tasks;

}
