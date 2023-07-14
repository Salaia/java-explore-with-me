package ru.practicum.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class HitDto {
    @NotBlank(message = "app may not be blank.")
    String app;
    @NotBlank(message = "uri may not be blank.")
    String uri;
    @NotBlank(message = "ip may not be blank.")
    String ip;
    @NotBlank(message = "timestamp may not be blank.")
    String timestamp;
}
