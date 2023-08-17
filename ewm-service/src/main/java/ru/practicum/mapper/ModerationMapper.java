package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.ModerationDto;
import ru.practicum.model.Moderation;

import java.time.LocalDateTime;

@UtilityClass
public class ModerationMapper {

    public Moderation toModeration(ModerationDto moderationDto, Long eventId) {
        return Moderation.builder()
                .eventId(eventId)
                .comment(moderationDto.getComment())
                .created(LocalDateTime.now())
                .build();
    }

    public ModerationDto toModerationDto(Moderation moderation) {
        return ModerationDto.builder()
                .id(moderation.getId())
                .comment(moderation.getComment())
                .created(moderation.getCreated())
                .eventId(moderation.getEventId())
                .build();
    }
}
