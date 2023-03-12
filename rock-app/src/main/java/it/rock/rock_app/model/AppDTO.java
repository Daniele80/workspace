package it.rock.rock_app.model;

import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AppDTO {

    private Long id;

    @Size(max = 255)
    private String description;

    private List<Long> projects;

}
