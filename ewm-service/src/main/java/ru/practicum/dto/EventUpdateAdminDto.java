package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.enums.StateAction;
import ru.practicum.model.Location;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventUpdateAdminDto {
    @Size(min = 20, max = 2000, message = "Annotation must be from 20 to 2000 characters long.")
    String annotation;

    Integer category;

    @Size(min = 20, max = 7000, message = "Description must be from 20 to 7000 characters long.")
    String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    Location location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    StateAction stateAction;

    @Size(min = 3, max = 120, message = "Title must be from 3 to 120 characters long.")
    String title;
}

