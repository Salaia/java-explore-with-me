package ru.practicum.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.mapper.HitMapper;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;


@Service
//@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatsServiceImpl implements StatsService {

    StatsRepository statsRepository;

    @Override
    public void hit(HitDto hitDto) {
        statsRepository.save(HitMapper.toHit(hitDto));
    }

    @Override
    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, Boolean unique, List<String> uris) {
        List<StatsDto> viewStatsList;

        if (uris == null || uris.isEmpty()) {
            if (unique) {
                viewStatsList = statsRepository.findViewStatsWithoutUrisUnigue(start, end, PageRequest.of(0, 10));
            } else {
                viewStatsList = statsRepository.findViewStatsWithoutUris(start, end, PageRequest.of(0, 10));
            }
        } else {
            if (unique) {
                viewStatsList = statsRepository.findViewStatsUniqueIp(start, end, uris);
            } else {
                viewStatsList = statsRepository.findViewStats(start, end, uris);
            }
        }

        return viewStatsList;
    }
}
