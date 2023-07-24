package ru.practicum.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationUpdateDto {

     Set<Long> events;

     Boolean pinned;

    @Size(max = 50, message = "Title may not be longer then 50 characters.")
    @NotBlank(message = "Title may not be blank.")
    String title;

}