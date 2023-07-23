package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
