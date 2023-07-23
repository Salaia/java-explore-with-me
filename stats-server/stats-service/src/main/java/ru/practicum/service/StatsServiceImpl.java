package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.Hit;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statRepository;

    @Override
    public EndpointHit create(EndpointHit endpointHit) {
        Hit hit = statRepository.save(HitMapper.toHit(endpointHit));
        return HitMapper.toHitDto(hit);
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, boolean unique, String[] uris) {
        List<ViewStats> viewStatsList;
        if (start.isAfter(end)) {
            throw new BadRequestException("Неверные датами начала и конца диапазона");
        }


        if (uris == null || uris.length == 0) {
            if (unique) {
                viewStatsList = statRepository.findViewStatsWithoutUrisUnique(start, end, PageRequest.of(0, 10));
            } else {
                viewStatsList = statRepository.findViewStatsWithoutUris(start, end, PageRequest.of(0, 10));
            }
        } else {
            if (unique) {
                viewStatsList = statRepository.findViewStatsUniqueIp(start, end, uris);
            } else {
                viewStatsList = statRepository.findViewStats(start, end, uris);
            }
        }

        return viewStatsList;
    }
}
