package ru.practicum.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

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

    @Size(min = 1, max = 50, message = "Title must be from 1 to 50 characters long.")
    String title;

}