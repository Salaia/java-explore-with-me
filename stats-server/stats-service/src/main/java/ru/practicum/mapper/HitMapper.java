package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.HitDto;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class HitMapper {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Hit toHit(HitDto hitDto) {


        return Hit.builder()
                .app(hitDto.getApp())
                .created(LocalDateTime.parse(hitDto.getTimestamp(), formatter))
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .build();
    }
}
