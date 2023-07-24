package ru.practicum.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.enums.StateAction;
import ru.practicum.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventCreateDto {
    @Size(min = 20, max = 2000, message = "Annotation must be from 20 to 2000 characters long.")
    @NotBlank(message = "Annotation may not be blank.")
    String annotation;

    Long category;

    @Size(min = 20, max = 7000, message = "Description must be from 20 to 7000 characters long.")
    @NotBlank(message = "Description may not be blank.")
    String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    Location location;

    Boolean paid = false;

    @PositiveOrZero
    Integer participantLimit = 0;

    Boolean requestModeration = true;

    StateAction stateAction;

    @Size(min = 3, max = 120, message = "Title must be from 3 to 120 characters long.")
    @NotBlank(message = "Title may not be empty.")
    String title;

}