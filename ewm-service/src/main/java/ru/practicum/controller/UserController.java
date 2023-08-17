package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.*;
import ru.practicum.service.EventService;
import ru.practicum.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final EventService eventService;
    private final RequestService requestService;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEventForUser(@PathVariable @Positive Long userId,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                               @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.debug("Requested endpoint: users/{userId}/events getEventByUserId userId = " + userId + ", from: " + from + ", size: " + size);
        return eventService.getEventForUser(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable @Positive Long userId,
                                    @RequestBody @Valid EventCreateDto eventDto) {
        log.debug("Requested endpoint: users/{userId}/events createEvent userId = " + userId + ", dto: " + eventDto);
        return eventService.createEvent(userId, eventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventByUserAndEventId(@PathVariable @Positive Long userId,
                                                 @PathVariable @Positive Long eventId) {
        log.debug("Requested endpoint: users/{userId}/events/{eventId} getEventByUserAndEventId userId = " + userId + ", eventId = " + eventId);
        return eventService.getEventByUserAndEventId(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable @Positive Long userId,
                                    @PathVariable @Positive Long eventId,
                                    @RequestBody @Validated EventUpdateUserDto eventUserRequest) {
        log.debug("Requested endpoint: users/{userId}/events/{eventId} updateEvent userId = " + userId + ", eventId = " + eventId);
        return eventService.updateEvent(userId, eventId, eventUserRequest);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestEventByUser(@PathVariable @Positive Long userId,
                                                               @PathVariable @Positive Long eventId) {
        log.debug("Requested endpoint: users/{userId}/events/{eventId}/requests getRequestEventByUser userId = " + userId +
                ", eventId = " + eventId);
        return requestService.getRequestEventByUser(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestEventByUser(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId,
            @RequestBody @Valid EventRequestStatusUpdateRequest statusUpdateRequest) {
        log.debug("Requested endpoint: users/{userId}/events/{eventId}/requests updateRequestEventByUser" +
                " userId = " + userId + ", eventId = " + eventId);

        return requestService.updateRequestStatus(userId, eventId, statusUpdateRequest);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getAllRequest(@PathVariable @Positive Long userId) {
        log.debug("Requested endpoint: users/{userId}/requests getAllRequest userId = " + userId);
        return requestService.getRequest(userId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addRequest(@PathVariable @Positive Long userId,
                                              @RequestParam(name = "eventId") @Positive Long eventId) {
        log.debug("Requested endpoint: users/{userId}/requests addRequest" +
                " userId = " + userId + ", eventId = " + eventId);
        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelledRequest(@PathVariable @Positive Long userId,
                                                    @PathVariable @Positive Long requestId) {
        log.debug("Requested endpoint: users/{userId}/requests/{requestId}/cancel cancelledRequest" +
                " userId = " + userId + ", requestId = " + requestId);
        return requestService.updateRequest(userId, requestId);
    }

}