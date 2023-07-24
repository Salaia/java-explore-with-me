package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.enums.StateSort;
import ru.practicum.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@Slf4j
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(defaultValue = "EVENT_DATE") StateSort sort,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size,
            HttpServletRequest request) {
        log.info("Requested endpoint: /events getEvents, text: " + text + ", categories: " + categories +
                ", paid: " + paid + ", rangeStart: " + rangeStart + ", rangeEnd: " + rangeEnd +
                ", onlyAvailable: " + onlyAvailable + ", sort: " + sort + ", from: " + from + ", size: " + size + ", dto: " + request);
        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventsById(@PathVariable @Positive Long eventId,
                                      HttpServletRequest request) {
        log.info("Requested endpoint: /events/{eventId} getEventsById c Id = " + eventId + ", dto: " + request);
        return eventService.getEventsById(eventId, request);
    }
}
