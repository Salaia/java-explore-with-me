package ru.practicum.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
// Переименовала эти дто: во всех обсуждениях в Пачке их называют именно так,
// потому что их так ментор называет в вебинаре
public class EndpointHit {
    private Long id;

    @NotBlank(message = "app may not be blank.")
    String app;
    @NotBlank(message = "uri may not be blank.")
    String uri;
    @NotBlank(message = "ip may not be blank.")
    String ip;
    @NotBlank(message = "timestamp may not be blank.")
    String timestamp;
}
