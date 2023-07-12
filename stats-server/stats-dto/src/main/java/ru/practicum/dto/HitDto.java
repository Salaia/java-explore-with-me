package ru.practicum.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HitDto {
    Long id;

    // Пока не понимаю, в чем это буду хранить, поэтому пока что в стрингах
    String app;
    String uri;
    String ip;
    LocalDateTime timestamp;
}
