package codeio.practise.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
//import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data //includes getters,setters,constructors
public class Todo {
    @Id
    @GeneratedValue
    Long id;
    @NotBlank
    @NotNull
    @Schema(name = "Title", example = "Spring boot todo project")
    String title;
    /* 
    @NotBlank
    @NotNull
    String Description;
    */
    Boolean isComplete;

    //@Email
    //String email;
    //like these there are many like min and max size and regex exp for phone numb
}
