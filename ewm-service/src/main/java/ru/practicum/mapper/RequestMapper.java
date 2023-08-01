package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.model.Request;

import java.time.LocalDateTime;

@UtilityClass
public class RequestMapper {
    public Request toRequest(Long userId, Long eventId) {
        return Request.builder()
                .created(LocalDateTime.now())
                .event(eventId)
                .requester(userId)
                .status(null)
                .build();
    }

    public ParticipationRequestDto toParticipationRequestDto(Request save) {
        return ParticipationRequestDto.builder()
                .id(save.getId())
                .created(save.getCreated())
                .event(save.getEvent())
                .requester(save.getRequester())
                .status(save.getStatus())
                .build();
    }
}
