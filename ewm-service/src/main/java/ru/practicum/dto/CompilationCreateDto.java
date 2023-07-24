package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationCreateDto {

    private Set<Long> events;
    private Boolean pinned = false;
    @Size(max = 50, message = "Title may not be longer then 50 characters.")
    @NotBlank(message = "Title may not be blank.")
    private String title;
}
