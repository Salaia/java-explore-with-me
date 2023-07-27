package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CompilationCreateDto;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.CompilationUpdateDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.exception.ValidationIdException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationService {

    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    public CompilationDto create(CompilationCreateDto compilationDto) {

        List<Event> eventList = eventRepository.findAllByIdIn(compilationDto.getEvents() == null ? new HashSet<>() : compilationDto.getEvents());
        Compilation compilation = CompilationMapper.toCompilation(compilationDto, eventList);
        Compilation compilationSave = compilationRepository.save(compilation);
        List<EventShortDto> eventShortDtoList = compilationSave.getEvents()
                .stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());

        return CompilationMapper.toCompilationDto(compilationSave, eventShortDtoList);
    }

    public void delete(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ValidationIdException("Compilation with id=" + compId + " was not found"));
        compilationRepository.delete(compilation);
    }

    public CompilationDto update(Long compId, CompilationUpdateDto compilationUpdateDto) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ValidationIdException("Compilation with id=" + compId + " was not found"));

        if (compilationUpdateDto.getTitle() != null) {
            compilation.setTitle(compilationUpdateDto.getTitle());
        }
        if (compilationUpdateDto.getTitle() != null) {
            compilation.setTitle(compilationUpdateDto.getTitle());
        }
        if (compilationUpdateDto.getEvents() != null) {
            List<Event> eventList = eventRepository.findAllByIdIn(compilationUpdateDto.getEvents());
            compilation.setEvents(eventList);
        }
        Compilation updateCompilation = compilationRepository.save(compilation);
        List<EventShortDto> eventShortDtoList = updateCompilation.getEvents()
                .stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());

        return CompilationMapper.toCompilationDto(updateCompilation, eventShortDtoList);
    }

    public List<CompilationDto> get(Boolean pinned, Integer from, Integer size) {
        List<Compilation> compilations;

        if (pinned != null) {
            compilations = compilationRepository.findAllByPinned(pinned, PageRequest.of(from, size));
        } else {
            compilations = compilationRepository.findAll(PageRequest.of(from, size)).getContent();
        }

        return compilations.stream()
                .map(compilation -> {
                    List<EventShortDto> eventShortDtoList = compilation.getEvents()
                            .stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
                    return CompilationMapper.toCompilationDto(compilation, eventShortDtoList);
                }).collect(Collectors.toList());

    }

    public CompilationDto getById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ValidationIdException("Compilation with id = " + compId + " was not found."));
        List<EventShortDto> eventShortDtoList = compilation.getEvents()
                .stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
        return CompilationMapper.toCompilationDto(compilation, eventShortDtoList);
    }
}
