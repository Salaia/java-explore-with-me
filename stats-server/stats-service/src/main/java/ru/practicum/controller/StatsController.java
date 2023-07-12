package ru.practicum.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatsController {

    StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity<String> hit(@RequestBody HitDto hitDto) {
        log.debug("Got request to save: " + hitDto);
        statsService.hit(hitDto);
        return new ResponseEntity<>("Hit endpoint: " + hitDto.getUri(), HttpStatus.CREATED);
    }


    @GetMapping("/stats")
    public List<StatsDto> findStats(@RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                    @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                    @RequestParam(name = "unique", defaultValue = "false") Boolean unique,
                                    @RequestParam(name = "uris", required = false) List<String> uris
    ) {
        log.debug("Got request to findStats from: " + start + " to " + end + "; unique: " + unique + "; on uris: " + uris);
        return statsService.getStats(start, end, unique, uris);
    }

}
