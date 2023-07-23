package ru.practicum.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatsController {

    StatsService statsService;

    @PostMapping("/hit")
    /*public ResponseEntity<String> hit(@Valid @RequestBody EndpointHit endpointHit) {
        log.debug("Got request to save: " + endpointHit);
        statsService.create(endpointHit);
        return new ResponseEntity<>("Hit endpoint: " + endpointHit.getUri(), HttpStatus.CREATED);
    }*/
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHit createInfo(@RequestBody @Valid EndpointHit endpointHit) {
        log.info("Получен запрос к эндпоинту \"/hits\" create");
        return statsService.create(endpointHit);
    }


    @GetMapping("/stats")
    public List<ViewStats> findStats(@RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                     @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                     @RequestParam(name = "unique", defaultValue = "false") boolean unique,
                                     @RequestParam(name = "uris", required = false) String[] uris
    ) {
        log.debug("Got request to findStats from: " + start + " to " + end + "; unique: " + unique + "; on uris: " + uris);
        return statsService.getStats(start, end, unique, uris);
    }

}
