package ru.practicum.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class HitDto {
    @NotBlank(message = "app may not be blank.")
    String app;
    @NotBlank(message = "uri may not be blank.")
    String uri;
    @NotBlank(message = "ip may not be blank.")
    String ip;
    @DateTimeFormat
    LocalDateTime timestamp;
}
